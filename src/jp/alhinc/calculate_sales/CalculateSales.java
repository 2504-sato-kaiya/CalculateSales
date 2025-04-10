package jp.alhinc.calculate_sales;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CalculateSales {

	// 支店定義ファイル名
	private static final String FILE_NAME_BRANCH_LST = "branch.lst";

	// 支店別集計ファイル名
	private static final String FILE_NAME_BRANCH_OUT = "branch.out";

	// エラーメッセージ
	private static final String UNKNOWN_ERROR = "予期せぬエラーが発生しました";
	private static final String FILE_NOT_EXIST = "支店定義ファイルが存在しません";
	private static final String FILE_INVALID_FORMAT = "支店定義ファイルのフォーマットが不正です";

	private static final String String = null;

	/**
	 * メインメソッド
	 *
	 * @param コマンドライン引数
	 */
	public static void
	(String[] args) {
		// 支店コードと支店名を保持するMap
		Map<String, String> branchNames = new HashMap<>();
		// 支店コードと売上金額を保持するMap
		Map<String, Long> branchSales = new HashMap<>();

		// 支店定義ファイル読み込み処理
		if(!readFile(args[0], FILE_NAME_BRANCH_LST, branchNames, branchSales)) {
			return;
		}

		// ※ここから集計処理を作成してください。(処理内容2-1、2-2)
		//files配列にargs[0](売上集計システムフォルダ内の全てのファイル)を追加(listFiles))
		File[] files = new File(args[0]).listFiles();
		//rcdFiles(ファイル名の情報)を格納する宣言(ArrayList)
		List<File> rcdFiles = new ArrayList<>();
		//files(全てのファイル)数分繰り返し処理(for)
		for(int i = 0; i < files.length ; i++) {
			//files[i](ファイル名1つずつ)にgetName(ファイル名を取得)して判定(matches)
			if(files[i].getName().matches("^[0-9]{8}.rcd$")) {
				//rcdFiles(ファイル名の情報)にfiles[i](ファイル名1つずつ)を追加(add)
				rcdFiles.add(files[i]);
			}

			BufferedReader br = null;

			try {
				File file = new File(args[0], rcdFiles.get(i).getName());

				FileReader fr = new FileReader(file);
				br = new BufferedReader(fr);

				String line;
				List<String> list = new ArrayList<>();
				//lineにbr(支店コードと売上金額)を1行ずつ読み込む(readline)
				while((line = br.readLine()) != null) {
					//listにline(支店コードと売上金額)を追加(add)
					list.add(line);
				//fileSaleにlist(売上金額)をLong(整数)に変換した値を追加(parseLong)
				long fileSale = Long.parseLong(list.get(1));
				//salesAmountに売上金額を入れたMap(branchSales)とfileSale(売上金額)を加算
				Long salesAmount = branchSales.get(list.get(0)) + fileSale;
				//branchSalesに支店コード(list.get(0))と売上金額(salesAmount)を追加(put)
				branchSales.put(list.get(0),salesAmount);
				}

			} catch(IOException e) {
				System.out.println(UNKNOWN_ERROR);
				return;
			} finally {
				// ファイルを開いている場合
				if(br != null) {
					try {
						// ファイルを閉じる
						br.close();
					} catch(IOException e) {
						System.out.println(UNKNOWN_ERROR);
						return;
					}
				}
			}
		}
	}

		// 支店別集計ファイル書き込み処理
//		if(!writeFile(args[0], FILE_NAME_BRANCH_OUT, branchNames, branchSales)) {
//			return;
//		}



	/**
	 * 支店定義ファイル読み込み処理
	 *
	 * @param フォルダパス
	 * @param ファイル名
	 * @param 支店コードと支店名を保持するMap
	 * @param 支店コードと売上金額を保持するMap
	 * @return 読み込み可否
	 */
	private static boolean readFile(String path, String fileName, Map<String, String> branchNames, Map<String, Long> branchSales) {
		BufferedReader br = null;

		try {
			File file = new File(path, fileName);
			FileReader fr = new FileReader(file);
			br = new BufferedReader(fr);

			String line;
			// 一行ずつ読み込む
			while((line = br.readLine()) != null) {
				String[] items = line.split(",");
				branchNames.put(items[0], items[1]);
				branchSales.put(items[0], 0L);
				// ※ここの読み込み処理を変更してください。(処理内容1-2)
				System.out.println(line);
			}

		} catch(IOException e) {
			System.out.println(UNKNOWN_ERROR);
			return false;
		} finally {
			// ファイルを開いている場合
			if(br != null) {
				try {
					// ファイルを閉じる
					br.close();
				} catch(IOException e) {
					System.out.println(UNKNOWN_ERROR);
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * 支店別集計ファイル書き込み処理
	 *
	 * @param フォルダパス
	 * @param ファイル名
	 * @param 支店コードと支店名を保持するMap
	 * @param 支店コードと売上金額を保持するMap
	 * @return 書き込み可否
	 */
	private static boolean writeFile(String path, String fileName, Map<String, String> branchNames, Map<String, Long> branchSales) {
		// ※ここに書き込み処理を作成してください。(処理内容3-1)
		BufferedWriter bw = null;

		try {
			File file = new File(path, fileName);
			FileWriter fw = new FileWriter(file);
			bw = new BufferedWriter(fw);

			//keyにMap(売上集計データ)の一覧を取得してkeyの数分繰り返す(KeySet)
			for (String key : branchNames.get(0).keySet()) {
				bw.write(key);
				bw.newLine();
				bw.write(key);
				bw.newLine();
				bw.write(key);
				bw.newLine();




			}
				//keyという変数には、Mapから取得したキーが代入されています。
				//拡張for⽂で繰り返されているので、1つ⽬のキーが取得できたら、
				//2つ⽬の取得...といったように、次々とkeyという変数に上書きされていきます。
		}catch(IOException e) {
			System.out.println(UNKNOWN_ERROR);
			return false;
		} finally {
			// ファイルを開いている場合
			if(bw != null) {
				try {
					// ファイルを閉じる
					bw.close();
				} catch(IOException e) {
					System.out.println(UNKNOWN_ERROR);
					return false;
				}
			}
		}
		return true;


			String line;

		return true;
	}

}
