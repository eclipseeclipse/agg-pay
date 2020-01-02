package com.bwton.agg.common.util;

import com.bwton.exception.BusinessException;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

import java.math.BigInteger;
import java.util.List;

public class ByteUtils {

	private ByteUtils(){}
	/**
	 * 合并byte数组 效率较低
	 * 
	 * @param byteArrs
	 * @param byteArrTotalLength
	 * @return
	 * @throws Exception
	 */
	public static byte[] mergeByteArr(List<byte[]> byteArrs, int byteArrTotalLength) {
		byte[] resultByteArr = new byte[byteArrTotalLength];
		int addNum = 0;
		for (byte[] t1b : byteArrs) {
			for (int i = 0; i < t1b.length; i++) {
				if (addNum > byteArrTotalLength) {
					throw new BusinessException("数组总长度参数错误！");
				} else {
					resultByteArr[addNum] = t1b[i];
					addNum++;
				}
			}
		}
		return resultByteArr;
	}

	/**
	 * 合并byte数组 效率较高
	 * 
	 * @param byteArrs
	 * @return
	 * @throws Exception
	 */
	public static byte[] mergeByteArr2(List<byte[]> byteArrs) {
		int byteLength = 0;
		for (byte[] t1b : byteArrs) {
			if (t1b != null && t1b.length > 0) {
				byteLength += t1b.length;
			}
		}
		byte[] resultByteArr = new byte[byteLength];
		int addNum = 0;
		for (byte[] t1b : byteArrs) {
			if (t1b != null && t1b.length > 0) {
				System.arraycopy(t1b, 0, resultByteArr, addNum, t1b.length);
				addNum += t1b.length;
			}
		}
		return resultByteArr;
	}

	/**
	 * 高低位长度计算 第一个字节为长度的高8位；第二个字节为长度的低8位
	 * 
	 * @param lengthByte
	 * @return
	 * @throws Exception
	 */
	public static int highAndLowCalculation(byte[] lengthByte, int startPos, int endPos) {
		int respInt = (int) (lengthByte[startPos] << 8) + (int) (lengthByte[endPos] & 0xFF);
		return respInt;
	}

	/**
	 * byte[]转16进制字符串
	 * 
	 * @param byteArr
	 * @return
	 */
	public static String byteArrToString(byte[] byteArr) {
		char[] resultChar = Hex.encodeHex(byteArr, false);
		return String.valueOf(resultChar);
	}

	/**
	 * 16进制字符串转byte[]
	 * 
	 * @param str
	 * @return
	 * @throws Exception
	 */
	public static byte[] stringToByteArr(String str) throws DecoderException {
		return Hex.decodeHex(str.toCharArray());
	}

	/**
	 * 截取指定长度byte数组
	 * 
	 * @param src
	 * @param begin
	 * @param count
	 * @return
	 */
	public static byte[] subBytes(byte[] src, int begin, int count) {
		byte[] bs = new byte[count];
		System.arraycopy(src, begin, bs, 0, count);
		return bs;
	}

	/**
	 * 截取指定长度byte数组转成16进制字符串
	 * 
	 * @param src
	 * @param begin
	 * @param count
	 * @return
	 */
	public static String subBytesToHexString(byte[] src, int begin, int count) {
		byte[] bs = new byte[count];
		System.arraycopy(src, begin, bs, 0, count);
		char[] resultChar = Hex.encodeHex(bs, false);
		return String.valueOf(resultChar);
	}

	/**
	 * 截取指定长度byte数组转成10进制数字
	 * 
	 * @param src
	 * @param begin
	 * @param count
	 * @return
	 */
	public static String subBytesToTenString(byte[] src, int begin, int count) {
		byte[] bs = new byte[count];
		System.arraycopy(src, begin, bs, 0, count);
		BigInteger bigInteger = new BigInteger(1, bs);
		return bigInteger.toString(10);
	}

}
