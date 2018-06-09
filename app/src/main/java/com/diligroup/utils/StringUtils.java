/*
 * Copyright (C) 2010 mAPPn.Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.diligroup.utils;

import android.text.TextUtils;

import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串处理工具类
 * 
 * @author andrew
 * @date    2011-4-26
 * @since   Version 0.7.0
 */
public class StringUtils {
//    public static boolean isMobileNumber(String mobiles) {
//        Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
//        Matcher m = p.matcher(mobiles);
//        return m.matches();
//
//    }
    /**
     * 验证手机号格式
     *
     * @param mobiles
     * @return
     */
    public static boolean isMobileNO(String mobiles) {
    /*
    移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
    联通：130、131、132、152、155、156、185、186
    电信：133、153、180、189、（1349卫通）
    总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9;新增4，7
    */
        String telRegex = "[1][34578]\\d{9}";//"[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        if (TextUtils.isEmpty(mobiles))
            return false;
        else
            return mobiles.matches(telRegex);
    }

    public static boolean isPsdFormat(String password) {
        Pattern p = Pattern.compile("(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{6,16}");
        Matcher m = p.matcher(password);
        return m.matches();
    }

    /**
     * 格式化文件大小
     */
    public static String formatSize(long size) {
        if (size < 1048576L)
            return new DecimalFormat("##0").format((float) size / 1024f) + "K";
        else if (size < 1073741824L)
            return new DecimalFormat("###0.##").format((float) size / 1048576f) + "M";
        else
            return new DecimalFormat("#######0.##").format((float) size / 1073741824f) + "G";
    }

    /**
     * <p>
     * Parse long value from string
     * </p>
     *
     * @param value string
     * @return long value
     */
    public static long getLong(String value) {
        if (value == null)
            return 0L;

        try {
            return Long.parseLong(value.trim());
        } catch (NumberFormatException e) {
            return 0L;
        }
    }

    /**
     * 从URI中获取文件名
     */
    public static String getFileNameFromUrl(final String url) {
        if (TextUtils.isEmpty(url)) {
            return "";
        }
        return url.substring(url.lastIndexOf("/") + 1);
    }

    /**
     * 格式化文件大小
     */
    public static String formatSize(String size) {
        return formatSize(getLong(size));
    }

    /**
     * 格式化下载量数据
     */
    public static String getDownloadInterval(int downloadNum) {
        if (downloadNum < 50) {
            return "小于50";
        } else if (downloadNum >= 50 && downloadNum < 100) {
            return "50 - 100";
        } else if (downloadNum >= 100 && downloadNum < 500) {
            return "100 - 500";
        } else if (downloadNum >= 500 && downloadNum < 1000) {
            return "500 - 1,000";
        } else if (downloadNum >= 1000 && downloadNum < 5000) {
            return "1,000 - 5,000";
        } else if (downloadNum >= 5000 && downloadNum < 10000) {
            return "5,000 - 10,000";
        } else if (downloadNum >= 10000 && downloadNum < 50000) {
            return "10,000 - 50,000";
        } else if (downloadNum >= 50000 && downloadNum < 250000) {
            return "50,000 - 250,000";
        } else {
            return "大于250,000";
        }
    }

    /**
     * 使用十六进制字符串生成字节数组
     *
     * @param hexString 十六进制字符串
     */
    public static byte[] fromHexString(String hexString) {
        if (hexString != null && (hexString.length() & 1) == 0) {
            char[] chars = hexString.toLowerCase().toCharArray();
            byte[] bytes = new byte[chars.length >>> 1];
            for (int i = 0, h = 0, l = 0; i < bytes.length; i++) {
                h = getAsciiCode(chars[i << 1]);
                l = getAsciiCode(chars[(i << 1) + 1]);
                if (h == -1 || l == -1) {
                    return null;
                }
                bytes[i] = (byte) ((h << 4) + l);
            }
            return bytes;
        }
        return null;
    }

    private static int getAsciiCode(char c) {
        int i = c - 48;
        i = i > 9 ? c - 87 : i;
        return i < 0 || i > 15 ? -1 : i;
    }

//    /** 用来将字节转换成 16 进制表示的字符 */
//    private static final char HEX_DIGIST[] = { '0', '1', '2', '3', '4', '5',
//            '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

    /**
     * 生成十六进制字符串
     *
     * @param source      字节数组
     * @param isUpperCase 是否使用大写字母
     */
    public static String toHexString(byte[] source, boolean isUpperCase) {
        if (source == null) {
            return "";
        }
        char str[] = new char[source.length << 1];
        int k = 0;
        for (int i = 0; i < source.length; i++) {
            byte byte0 = source[i];
            str[k++] = HEX_DIGIST[byte0 >>> 4 & 0xf];
            str[k++] = HEX_DIGIST[byte0 & 0xf];
        }
        String s = new String(str);
        if (isUpperCase)
            return s.toUpperCase();
        else
            return s;
    }

    /**
     * 用来将字节转换成 16 进制表示的字符
     */
    private static final char HEX_DIGIST[] = {'0', '1', '2', '3', '4', '5',
            '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    /**
     * Encodes the given string into a sequence of bytes using the ISO-8859-1 charset, storing the result into a new
     * byte array.
     *
     * @param string the String to encode
     * @return encoded bytes
     * @throws IllegalStateException Thrown when the charset is missing, which should be never according the the Java specification.
     * @see <a href="http://java.sun.com/j2se/1.4.2/docs/api/java/nio/charset/Charset.html">Standard charsets</a>
     * @see #getBytesUnchecked(String, String)
     */
    public static byte[] getBytesIso8859_1(String string) {
        return StringUtils.getBytesUnchecked(string, CharEncoding.ISO_8859_1);
    }

    /**
     * Encodes the given string into a sequence of bytes using the US-ASCII charset, storing the result into a new byte
     * array.
     *
     * @param string the String to encode
     * @return encoded bytes
     * @throws IllegalStateException Thrown when the charset is missing, which should be never according the the Java specification.
     * @see <a href="http://java.sun.com/j2se/1.4.2/docs/api/java/nio/charset/Charset.html">Standard charsets</a>
     * @see #getBytesUnchecked(String, String)
     */
    public static byte[] getBytesUsAscii(String string) {
        return StringUtils.getBytesUnchecked(string, CharEncoding.US_ASCII);
    }

    /**
     * Encodes the given string into a sequence of bytes using the UTF-16 charset, storing the result into a new byte
     * array.
     *
     * @param string the String to encode
     * @return encoded bytes
     * @throws IllegalStateException Thrown when the charset is missing, which should be never according the the Java specification.
     * @see <a href="http://java.sun.com/j2se/1.4.2/docs/api/java/nio/charset/Charset.html">Standard charsets</a>
     * @see #getBytesUnchecked(String, String)
     */
    public static byte[] getBytesUtf16(String string) {
        return StringUtils.getBytesUnchecked(string, CharEncoding.UTF_16);
    }

    /**
     * Encodes the given string into a sequence of bytes using the UTF-16BE charset, storing the result into a new byte
     * array.
     *
     * @param string the String to encode
     * @return encoded bytes
     * @throws IllegalStateException Thrown when the charset is missing, which should be never according the the Java specification.
     * @see <a href="http://java.sun.com/j2se/1.4.2/docs/api/java/nio/charset/Charset.html">Standard charsets</a>
     * @see #getBytesUnchecked(String, String)
     */
    public static byte[] getBytesUtf16Be(String string) {
        return StringUtils.getBytesUnchecked(string, CharEncoding.UTF_16BE);
    }

    /**
     * Encodes the given string into a sequence of bytes using the UTF-16LE charset, storing the result into a new byte
     * array.
     *
     * @param string the String to encode
     * @return encoded bytes
     * @throws IllegalStateException Thrown when the charset is missing, which should be never according the the Java specification.
     * @see <a href="http://java.sun.com/j2se/1.4.2/docs/api/java/nio/charset/Charset.html">Standard charsets</a>
     * @see #getBytesUnchecked(String, String)
     */
    public static byte[] getBytesUtf16Le(String string) {
        return StringUtils.getBytesUnchecked(string, CharEncoding.UTF_16LE);
    }

    /**
     * Encodes the given string into a sequence of bytes using the UTF-8 charset, storing the result into a new byte
     * array.
     *
     * @param string the String to encode
     * @return encoded bytes
     * @throws IllegalStateException Thrown when the charset is missing, which should be never according the the Java specification.
     * @see <a href="http://java.sun.com/j2se/1.4.2/docs/api/java/nio/charset/Charset.html">Standard charsets</a>
     * @see #getBytesUnchecked(String, String)
     */
    public static byte[] getBytesUtf8(String string) {
        return StringUtils.getBytesUnchecked(string, CharEncoding.UTF_8);
    }

    /**
     * Encodes the given string into a sequence of bytes using the named charset, storing the result into a new byte
     * array.
     * <p>
     * This method catches {@link UnsupportedEncodingException} and rethrows it as {@link IllegalStateException}, which
     * should never happen for a required charset name. Use this method when the encoding is required to be in the JRE.
     * </p>
     *
     * @param string      the String to encode
     * @param charsetName The name of a required {@link java.nio.charset.Charset}
     * @return encoded bytes
     * @throws IllegalStateException Thrown when a {@link UnsupportedEncodingException} is caught, which should never happen for a
     *                               required charset name.
     * @see CharEncoding
     * @see String#getBytes(String)
     */
    public static byte[] getBytesUnchecked(String string, String charsetName) {
        if (string == null) {
            return null;
        }
        try {
            return string.getBytes(charsetName);
        } catch (UnsupportedEncodingException e) {
            throw StringUtils.newIllegalStateException(charsetName, e);
        }
    }

    private static IllegalStateException newIllegalStateException(String charsetName, UnsupportedEncodingException e) {
        return new IllegalStateException(charsetName + ": " + e);
    }

    /**
     * Constructs a new <code>String</code> by decoding the specified array of bytes using the given charset.
     * <p>
     * This method catches {@link UnsupportedEncodingException} and re-throws it as {@link IllegalStateException}, which
     * should never happen for a required charset name. Use this method when the encoding is required to be in the JRE.
     * </p>
     *
     * @param bytes       The bytes to be decoded into characters
     * @param charsetName The name of a required {@link java.nio.charset.Charset}
     * @return A new <code>String</code> decoded from the specified array of bytes using the given charset.
     * @throws IllegalStateException Thrown when a {@link UnsupportedEncodingException} is caught, which should never happen for a
     *                               required charset name.
     * @see CharEncoding
     * @see String#String(byte[], String)
     */
    public static String newString(byte[] bytes, String charsetName) {
        if (bytes == null) {
            return null;
        }
        try {
            return new String(bytes, charsetName);
        } catch (UnsupportedEncodingException e) {
            throw StringUtils.newIllegalStateException(charsetName, e);
        }
    }

    /**
     * Constructs a new <code>String</code> by decoding the specified array of bytes using the ISO-8859-1 charset.
     *
     * @param bytes The bytes to be decoded into characters
     * @return A new <code>String</code> decoded from the specified array of bytes using the given charset.
     * @throws IllegalStateException Thrown when a {@link UnsupportedEncodingException} is caught, which should never happen since the
     *                               charset is required.
     */
    public static String newStringIso8859_1(byte[] bytes) {
        return StringUtils.newString(bytes, CharEncoding.ISO_8859_1);
    }

    /**
     * Constructs a new <code>String</code> by decoding the specified array of bytes using the US-ASCII charset.
     *
     * @param bytes The bytes to be decoded into characters
     * @return A new <code>String</code> decoded from the specified array of bytes using the given charset.
     * @throws IllegalStateException Thrown when a {@link UnsupportedEncodingException} is caught, which should never happen since the
     *                               charset is required.
     */
    public static String newStringUsAscii(byte[] bytes) {
        return StringUtils.newString(bytes, CharEncoding.US_ASCII);
    }

    /**
     * Constructs a new <code>String</code> by decoding the specified array of bytes using the UTF-16 charset.
     *
     * @param bytes The bytes to be decoded into characters
     * @return A new <code>String</code> decoded from the specified array of bytes using the given charset.
     * @throws IllegalStateException Thrown when a {@link UnsupportedEncodingException} is caught, which should never happen since the
     *                               charset is required.
     */
    public static String newStringUtf16(byte[] bytes) {
        return StringUtils.newString(bytes, CharEncoding.UTF_16);
    }

    /**
     * Constructs a new <code>String</code> by decoding the specified array of bytes using the UTF-16BE charset.
     *
     * @param bytes The bytes to be decoded into characters
     * @return A new <code>String</code> decoded from the specified array of bytes using the given charset.
     * @throws IllegalStateException Thrown when a {@link UnsupportedEncodingException} is caught, which should never happen since the
     *                               charset is required.
     */
    public static String newStringUtf16Be(byte[] bytes) {
        return StringUtils.newString(bytes, CharEncoding.UTF_16BE);
    }

    /**
     * Constructs a new <code>String</code> by decoding the specified array of bytes using the UTF-16LE charset.
     *
     * @param bytes The bytes to be decoded into characters
     * @return A new <code>String</code> decoded from the specified array of bytes using the given charset.
     * @throws IllegalStateException Thrown when a {@link UnsupportedEncodingException} is caught, which should never happen since the
     *                               charset is required.
     */
    public static String newStringUtf16Le(byte[] bytes) {
        return StringUtils.newString(bytes, CharEncoding.UTF_16LE);
    }

    /**
     * Constructs a new <code>String</code> by decoding the specified array of bytes using the UTF-8 charset.
     *
     * @param bytes The bytes to be decoded into characters
     * @return A new <code>String</code> decoded from the specified array of bytes using the given charset.
     * @throws IllegalStateException Thrown when a {@link UnsupportedEncodingException} is caught, which should never happen since the
     *                               charset is required.
     */
    public static String newStringUtf8(byte[] bytes) {
        return StringUtils.newString(bytes, CharEncoding.UTF_8);
    }

    /**
     * 集合转换为字符串
     * @param list
     * @param separator
     * @return
     */
    public static String listToString(List<String> list, char separator) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            if (i == list.size() - 1) {
                sb.append(list.get(i));
            } else {
                sb.append(list.get(i));
                sb.append(separator);
            }
        }
        return sb.toString();
    }
    /**
     * 保留两位小数
     * @param price
     * @return
     */
    public static String DecimalTwoFloat(float price) {
        DecimalFormat df = new DecimalFormat("0.00");// 格式化小数，不足的补0
        return df.format(price);
    }
    /**
     * 取出hashmap中所有key==日期字符串
     * @param
     * @return
     */
    public static String getMapKeystr(TreeMap map) {
        Set<String> keys = map.keySet();// 得到全部的key
        StringBuilder sb=new StringBuilder();
//        String str;
        if(keys.size()<=2){
            Iterator<String> iter = keys.iterator();
            while (iter.hasNext()) {
                sb.append(iter.next()+",");
            }
        }
        return sb.toString().trim();
    }
}
