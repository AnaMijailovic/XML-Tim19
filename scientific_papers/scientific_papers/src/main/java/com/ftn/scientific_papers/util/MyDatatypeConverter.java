package com.ftn.scientific_papers.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 
 * Konverter klasa za java.utilDate, parse odnosno 
 * print metode moraju biti statiÄ�ke.
 *
 */
public class MyDatatypeConverter {

	private static final SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
	private static final SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy");

	/**
	 * Metoda parsira tekstualnu vrednost datuma.
	 * @param value tekstualni reprezent datuma
	 * @return VraÄ‡a datumsku vrednost tekstualnog reprezenta.
	 */
	public static Date parseDate(String value) {
		try {
			return dateFormat1.parse(value);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}

	}

	/**
	 * Metoda Å¡tampa datum u definisanom formatu.
	 * @param value datumska vrednost
	 * @return VraÄ‡a tekstualni reprezent datuma.
	 */
	public static String printDate(Date value) {
		if (value != null)
			return dateFormat1.format(value);
		else
			return null;

	}

	/**
	 * Metoda parsira tekstualnu vrednost datuma.
	 * @param value tekstualni reprezent datuma
	 * @return VraÄ‡a datumsku vrednost tekstualnog reprezenta.
	 */
	public static Date parseYear(String value) {
		try {
			return dateFormat2.parse(value);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}

	}

	/**
	 * Metoda Å¡tampa datum u definisanom formatu.
	 * @param value datumska vrednost
	 * @return VraÄ‡a tekstualni reprezent datuma.
	 */
	public static String printYear(Date value) {
		if (value != null)
			return dateFormat2.format(value);
		else
			return null;

	}
}
