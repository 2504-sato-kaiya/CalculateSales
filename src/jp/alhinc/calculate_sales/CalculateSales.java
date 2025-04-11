package jp.alhinc.calculate_sales;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
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
	private static final String NOT_CONSECTIVE_NUMBERS = "売上ファイル名が連番になっていません";
	private static final String SALE_AMOUNT_OVERFLOW = "合計⾦額が10桁を超えました";
	private static final String FILE_INVALID_BRANCH_CODE = "の⽀店コードが不正です";
	private static final String FILE_INVALID_BRANCH_FORMAT = "のフォーマットが不正です";


	private static final String String = null;

	/**
	 * メインメソッド
	 *
	 * @param コマンドライン引数
	 */
	public static void main(String[] args) {

		// 支店コードと支店名を保持するMap
		Map<String, String> branchNames = new HashMap<>();
		// 支店コードと売上金額を保持するMap
		Map<String, Long> branchSales = new HashMap<>();
		// 支店定義ファイル読み込み処理
		if(!readFile(args[0], FILE_NAME_BRANCH_LST, branchNames, branchSales)) {

			return;

		}

		//args(支店定義ファイル)が読み込まれていないか判定(length != 1)
		if (args.length != 1) {

			//UNKNOWN_ERROR(予期せぬエラーが発生しました)を出力
			System.out.println(UNKNOWN_ERROR);
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
			if(files[i].getName().matches("^[0-9]{8}[.]rcd$")) {

				//rcdFiles(ファイル名の情報)にfiles[i](ファイル名1つずつ)を追加(add)
				rcdFiles.add(files[i]);

			}
		}

		//ファイル名の情報(rcdFiles)を昇順にソート(Collections.sort)
		Collections.sort(rcdFiles);
		for(int i = 0; i < rcdFiles.size() - 1; i++) {

			//ファイル名の情報(rcdFiles.get(i).getName())から
			//数字8文字を切り出し(substring(0, 8))、int型に変換
			int former = Integer.parseInt(rcdFiles.get(i).getName().substring(0, 8));
			//次のファイル名の情報(rcdFiles.get(i + 1).getName())から
			//数字8文字を切り出し(substring(0, 8))、int型に変換
			int latter = Integer.parseInt(rcdFiles.get(i + 1).getName().substring(0, 8));
			//次のファイル名の情報(latter)-ファイル名の情報(former)が1でないか判定
			if((latter - former) != 1) {

				//NOT_CONSECTIVE_NUMBERS(売上ファイル名が連番になっていません)を出力
				System.out.println(NOT_CONSECTIVE_NUMBERS);
				return;

			}
		}

		for(int i = 0; i < rcdFiles.size(); i++) {

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

				}

					//list(売上データ)が2行でないか確認(size() != 2)
					if(list.size() != 2) {

						//該当ファイル名(rcdFiles.get(i).getName())のフォーマットが不正ですを出力
						System.out.println(rcdFiles.get(i).getName() + FILE_INVALID_BRANCH_FORMAT);
						return;

					}

					//売上ファイルの支店コード(list.get(0))が、
					//支店コードを入れたMap(branchNames)に存在していないか確認(containsKey)
					if (!branchNames.containsKey(list.get(0))) {

						//該当ファイル名(rcdFiles.get(i).getName())の⽀店コードが不正ですを出力
						System.out.println(rcdFiles.get(i).getName() + FILE_INVALID_BRANCH_CODE);
						return;

					}

					//list.get(1)(売上金額)が数字でないか判定(matches("[0-9]"))
					if(!list.get(1).matches("[0-9]")) {
						//UNKNOWN_ERROR(予期せぬエラーが発生しました)を出力
						System.out.println(UNKNOWN_ERROR);
						return;

					}

				//fileSaleにlist.get(1)(売上金額)をLong(整数)に変換した値を追加(parseLong)
				long fileSale = Long.parseLong(list.get(1));
				//売上ファイルの売上金額(list.get(0))が、
				//支店コードを入れたMap(branchNames)に存在していないか確認(containsKey)


				//salesAmountに売上金額を入れたMap(branchSales)とfileSale(売上金額)を加算
				Long saleAmount = branchSales.get(list.get(0)) + fileSale;
				//売上金額(salesAmount)が11桁以上でないか確認
				if(saleAmount >= 10000000000L){

					//合計⾦額が10桁を超えました(EXCEEDS_SALE_AMOUNT)を出力
					System.out.println(SALE_AMOUNT_OVERFLOW);
					return;

				}

				//branchSalesに支店コード(list.get(0))と売上金額(salesAmount)を追加(put)
				branchSales.put(list.get(0),saleAmount);

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

		// 支店別集計ファイル書き込み処理
		if(!writeFile(args[0], FILE_NAME_BRANCH_OUT, branchNames, branchSales)) {

			return;

		}
	}

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
			if(!file.exists()) {

				//支店定義ファイルが存在しません(FILE_NOT_EXIST)を出力
				System.out.println(FILE_NOT_EXIST);
				return false;

			}



			FileReader fr = new FileReader(file);
			br = new BufferedReader(fr);
			String line;
			// 一行ずつ読み込む
			while((line = br.readLine()) != null) {

				String[] items = line.split(",");
				//⽀店定義ファイルが「,」で区切られていないか判定(length != 2)
				//支店コード(!items[0])が数字3桁("^[0-9]{3}")でないか判定(matches)

				if((items.length != 2) || (!items[0].matches("^[0-9]{3}"))){

					//支店定義ファイルのフォーマットが不正です(FILE_INVALID_FORMAT)を出力
					System.out.println(FILE_INVALID_FORMAT);
					return false;

				}

				branchNames.put(items[0], items[1]);
				branchSales.put(items[0], 0L);



			}
				// ※ここの読み込み処理を変更してください。(処理内容1-2)

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
			for (String key : branchNames.keySet()) {

				//支店別集計ファイルにkey(支店名)+branchNames(1)(支店名)+branchSales(1)(合計金額)を書き込む
				bw.write(key + "," + branchNames.get(key) + "," + branchSales.get(key));
				//支店別集計ファイルに改行を書き込む
				bw.newLine();

			}

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

	}
}
