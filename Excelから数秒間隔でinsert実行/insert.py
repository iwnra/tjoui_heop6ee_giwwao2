import pandas as pd
import time
import json
import argparse

# DB接続（MySQLのみ）
def get_connection(config):
    import pymysql
    db = config["db"]
    return pymysql.connect(
        host=db["host"],
        port=db.get("port", 3306),
        user=db["user"],
        password=db["password"],
        database=db["dbname"],
        charset='utf8mb4'
    )

# Excelからデータを読み込んでINSERT
def insert_from_excel(excel_path, conn, limit=None, interval=1, start_row=2, end_row=None):
    excel_file = pd.ExcelFile(excel_path)
    sheet_name = excel_file.sheet_names[0]
    table_name = sheet_name

    # 1行目をカラム名として読み込み
    df = pd.read_excel(excel_file, sheet_name=sheet_name, header=0)

    if df.empty:
        raise ValueError("Excelファイルにレコードが存在しません")

    # start_row/end_rowを0始まりに変換
    start_idx = max(start_row - 2, 0)
    end_idx = (end_row - 2) if end_row is not None else None
    df = df.iloc[start_idx:end_idx+1 if end_idx is not None else None]

    cursor = conn.cursor()
    try:
        for i, row in enumerate(df.itertuples(index=False), start=1):
            if limit is not None and i > limit:
                break

            columns = df.columns.tolist()
            columns_to_insert = []
            placeholders = []
            values = []

            for col, val in zip(columns, row):
                if pd.isna(val) or str(val).strip() == "« NULL »":
                    continue
                elif isinstance(val, str):
                    val_strip = val.strip().lower()
                    if val_strip == 'now()':
                        columns_to_insert.append(f"`{col}`")
                        placeholders.append("NOW()")
                    elif val_strip == 'uuid()':
                        columns_to_insert.append(f"`{col}`")
                        placeholders.append("UUID()")
                    elif val_strip == '""':
                        columns_to_insert.append(f"`{col}`")
                        placeholders.append("%s")
                        values.append("")
                    else:
                        columns_to_insert.append(f"`{col}`")
                        placeholders.append("%s")
                        values.append(val)
                else:
                    columns_to_insert.append(f"`{col}`")
                    placeholders.append("%s")
                    values.append(val)

            if not columns_to_insert:
                print(f"[{i}] Skipped empty row")
                continue

            sql = f"INSERT INTO {table_name} ({', '.join(columns_to_insert)}) VALUES ({', '.join(placeholders)})"
            print(f"\n[{i}] Executing:\n{sql}\nValues: {values}")

            try:
                cursor.execute(sql, values)
                conn.commit()
                print(f"  -> Inserted row {i}")
            except Exception as e:
                print(f"  -> Error inserting row {i}: {e}")

            time.sleep(interval)

    finally:
        cursor.close()
        conn.close()

if __name__ == "__main__":
    parser = argparse.ArgumentParser(description="Insert data from Excel into MySQL using config.json")
    parser.add_argument("--config", default="config.json", help="Path to config.json")

    args = parser.parse_args()

    with open(args.config, "r", encoding="utf-8") as f:
        config = json.load(f)

    conn = get_connection(config)
    interval = config.get("interval", 1)
    start_row = config.get("start_row", 2)
    end_row = config.get("end_row", None)

    insert_from_excel(config["excel_path"], conn, config.get("limit"), interval, start_row, end_row)
