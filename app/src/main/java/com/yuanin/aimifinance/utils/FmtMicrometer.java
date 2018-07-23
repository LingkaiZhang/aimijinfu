package com.yuanin.aimifinance.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;

public class FmtMicrometer {

	/**
	* 字符串数字三分位处理
	**/
	public static void main(String[] args) throws ParseException {

		System.out.println(fmtMicrometer(String.valueOf(123456.123)));
		System.out.println(fmtMicrometer(2.135 + ""));
		System.out.println(fmtMicrometer(2.1 + ""));

	}

	/**

	 * @Title: fmtMicrometer

	 * @Description: 格式化数字为千分位

	 * @param text

	 * @return    设定文件

	 * @return String    返回类型

	 */

	public static String fmtMicrometer(String text) {

		DecimalFormat df = null;

		if (text.indexOf(".") > 0) {

			if (text.length() - text.indexOf(".") - 1 == 0) {

				df = new DecimalFormat("###,##0.");

			} else if (text.length() - text.indexOf(".") - 1 == 1) {

				df = new DecimalFormat("###,##0.0");

			} else {

				df = new DecimalFormat("###,##0.00");

			}

		} else {

			df = new DecimalFormat("###,##0");

		}

		double number = 0.0;

		try {

			number = Double.parseDouble(text);

		} catch (Exception e) {

			number = 0.0;

		}

		return df.format(number);

	}

	/**
	 * 字符串保留两位小数
	 * */

	public static String format5(double value) {

		return String.format("%.2f", value).toString();
	}

	/**
	 * 字符串保留两位小数
	 * */

	public static String format6(String num) {
		DecimalFormat format = new DecimalFormat("0.00");
		String a = format.format(new BigDecimal( num));
		return a ;
	}
}