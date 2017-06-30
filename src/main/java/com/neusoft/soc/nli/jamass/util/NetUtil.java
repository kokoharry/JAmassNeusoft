/*-
 * Copyright (c) 2008-2010 Neusoft SOC
 * All rights reserved. 
 * NetUtil.java
 * Date: 2010-06-11
 * Author: Zhuang Di
 */
package com.neusoft.soc.nli.jamass.util;

import java.util.StringTokenizer;

/**
 * NetUtil
 * <p>
 * Date: 2010-06-11,16:35:34 +0800
 * 
 * @author Zhuang Di
 * @version 1.0
 */
public class NetUtil {

    /**
     * 将点分十进制IP字符串转换成long型
     */
    public static long getLongValuefromIPStr(String addr) {
        try {
            StringTokenizer st = new StringTokenizer(addr, ".");
            long[] ip = new long[st.countTokens()];
            int i = 0;
            while (st.hasMoreTokens()) {
                ip[i] = Integer.parseInt(st.nextToken());
                i++;
            }
            return (ip[0] << 24) + (ip[1] << 16) + (ip[2] << 8) + ip[3];
        } catch (Throwable e) {
            return 0;
        }
    }

    public static String normalizeIPV6(byte[] ipV6) {
        return InetV6AddressUtil.normalizeIPV6(ipV6);
    }

    public static boolean isIPv6LiteralAddress(String src) {
        return InetV6AddressUtil.isIPv6LiteralAddress(src);
    }

    public static byte[] textToNumericFormatV6(String src) {
        return InetV6AddressUtil.textToNumericFormatV6(src);
    }

//    /**
//     * 取得下一个IP，比当前IP值大1.
//     *
//     * @param ip
//     *            当前IP
//     * @return 下一个IP
//     */
//    public static String getNextIP(String ip) {
//        int iparray[] = new int[4];
//        ipToint(ip, iparray);
//        if (++iparray[3] == 256) {
//            iparray[3] = 0;
//            if (++iparray[2] == 256) {
//                iparray[2] = 0;
//                if (++iparray[1] == 256) {
//                    iparray[1] = 0;
//                    if (++iparray[0] == 256)
//                        iparray[0] = 0;
//                }
//            }
//        }
//        return intToip(iparray);
//    }
//
//    /**
//     * 将ip数组转成字符串.
//     */
//    public static String intToip(int iparray[]) {
//        String ip = "";
//        for (int i = 0; i < 4; i++) {
//            ip = (new StringBuilder(String.valueOf(ip))).append(iparray[i])
//                .toString();
//            if (i != 3)
//                ip = (new StringBuilder(String.valueOf(ip))).append(".")
//                    .toString();
//        }
//
//        return ip;
//    }
//
//    /**
//     * 将字符串IP转成数组IP
//     */
//    public static void ipToint(String ip, int iparray[]) {
//        int pos = ip.indexOf(".");
//        iparray[0] = Integer.parseInt(ip.substring(0, pos));
//        int nextpos = ip.indexOf(".", pos + 1);
//        iparray[1] = Integer.parseInt(ip.substring(pos + 1, nextpos));
//        pos = nextpos;
//        nextpos = ip.indexOf(".", pos + 1);
//        iparray[2] = Integer.parseInt(ip.substring(pos + 1, nextpos));
//        iparray[3] = Integer.parseInt(ip.substring(nextpos + 1, ip.length()));
//    }
//
//    /**
//     * 将字符串IP转成字节数组IP
//     */
//    public static byte[] getIpBytes(String ip) {
//        byte[] bytes = new byte[4];
//        int pos = ip.indexOf(".");
//        bytes[0] = (byte) (0x0FF & Integer.parseInt(ip.substring(0, pos)));
//        int nextpos = ip.indexOf(".", pos + 1);
//        bytes[1] = (byte) (0x0FF & Integer.parseInt(ip.substring(pos + 1,
//            nextpos)));
//        pos = nextpos;
//        nextpos = ip.indexOf(".", pos + 1);
//        bytes[2] = (byte) (0x0FF & Integer.parseInt(ip.substring(pos + 1,
//            nextpos)));
//        bytes[3] = (byte) (0x0FF & Integer.parseInt(ip.substring(nextpos + 1,
//            ip.length())));
//        return bytes;
//    }
//
//    /**
//     * 得到子网ID
//     */
//    public static String getSubnetIP(String ip, String mask) {
//        if (mask == null) {
//            return null;
//        }
//        int arrayip[] = new int[4];
//        int arraymask[] = new int[4];
//        int arraytemp[] = new int[4];
//        ipToint(ip, arrayip);
//        ipToint(mask, arraymask);
//        arraytemp[0] = arrayip[0] & arraymask[0];
//        arraytemp[1] = arrayip[1] & arraymask[1];
//        arraytemp[2] = arrayip[2] & arraymask[2];
//        arraytemp[3] = arrayip[3] & arraymask[3];
//        return intToip(arraytemp);
//    }
//
//    /**
//     * 比较两个IP的大小.
//     */
//    public static int compareIP(String ip1, String ip2) {
//        int arrayip1[] = new int[4];
//        int arrayip2[] = new int[4];
//        ipToint(ip1, arrayip1);
//        ipToint(ip2, arrayip2);
//        int k = 0;
//        for (int i = 0; i < 4; i++) {
//            if (arrayip1[i] > arrayip2[i])
//                return 1;
//            if (arrayip1[i] == arrayip2[i])
//                k++;
//        }
//
//        return k != 4 ? -1 : 0;
//    }
//
//    /**
//     * 判断两个IP是否是同一子网
//     */
//    public static boolean isSameSubnet(String ip1, String ip2, String mask) {
//        return getSubnetIP(ip1, mask).equals(getSubnetIP(ip2, mask));
//    }
//
//
//
//    /**
//     *
//     * @param ip
//     *            负值IP
//     * @return 真实IP
//     */
//    public static long fromRealIP(int ip) {
//        if (ip > 0) {
//            return ip;
//        }
//        return (long) (ip + Math.pow(2, 32));
//    }
//
//    /**
//     * 将long型IP转换成点分十进制字符串型.
//     */
//    public static String toIP(long ip) {
//        long tmp4 = ip % 256;
//        ip = (ip - tmp4) / 256;
//        long tmp3 = ip % 256;
//        ip = (ip - tmp3) / 256;
//        long tmp2 = ip % 256;
//        ip = (ip - tmp2) / 256;
//        long tmp1 = ip % 256;
//        return tmp1 + "." + tmp2 + "." + tmp3 + "." + tmp4;
//    }
//
//    /**
//     * 数组值转成整型数值
//     */
//    public static int getIntValue(int inttemp[]) {
//        int j = 0;
//        for (int i = 0; i <= 7; i++)
//            j += (int) Math.pow(2D, (double) i * 1.0D) * inttemp[i];
//
//        return j;
//    }
//
//    /**
//     * Make the interface name from ifDescr
//     */
//    public static String getIfShort(String ifIdx, String ifDescr, String type) {
//        if (ifDescr == null || ifDescr.length() < 1) {
//            return ifIdx;
//        }
//        if (type == null) {
//            type = "";
//        }
//        StringBuilder sb = new StringBuilder();
//        // 1st number pos
//        int fDigital = 0;
//        for (; fDigital < ifDescr.length(); fDigital++) {
//            char c = ifDescr.charAt(fDigital);
//            if (c >= '0' && c <= '9') {
//                break;
//            }
//        }
//        // reduce the non-number prefix into 2 char at most.
//        if ("6,62,117".indexOf(type) > -1 && ifDescr.indexOf("/") > fDigital) {
//            // '/' exists, erase all non-digital char
//        } else if (fDigital > 2) {
//            // reserve leading 2 char.
//            sb.append(ifDescr.substring(0, 2));
//        } else {
//            sb.append(ifDescr.substring(0, fDigital));
//        }
//        // Only reserve: # -./123456789:
//        for (int i = fDigital; i < ifDescr.length(); i++) {
//            char c = ifDescr.charAt(i);
//            if (c == '#' || (c >= '-' && c <= ':')) {
//                sb.append(c);
//            }
//        }
//
//        String result = sb.toString().toLowerCase();
//        if (result.startsWith("vl")) {
//            return "v" + result.substring(2);
//        } else if (result.startsWith("v")) {
//            return result;
//        }
//
//        return sb.toString();
//    }
//
//    public String getIpRangeStr(long startIp, long endIp) {
//        String str = null;
//        long low = startIp;
//        long high = endIp;
//
//        long tmp4 = low % 256;
//        low = (low - tmp4) / 256;
//        long tmp3 = low % 256;
//        low = (low - tmp3) / 256;
//        long tmp2 = low % 256;
//        low = (low - tmp2) / 256;
//        long tmp1 = low % 256;
//
//        long tmp8 = high % 256;
//        high = (high - tmp8) / 256;
//        long tmp7 = high % 256;
//        high = (high - tmp7) / 256;
//        long tmp6 = high % 256;
//        high = (high - tmp6) / 256;
//        long tmp5 = high % 256;
//
//        if ((tmp7 - tmp3) == 0) {
//            str = tmp1 + "." + tmp2 + "." + tmp3 + "." + tmp4 + "-" + tmp8
//                + " ";
//
//        } else if ((tmp7 - tmp3) == 1) {
//            str = tmp1 + "." + tmp2 + "." + tmp3 + "." + tmp4 + "-255 " + tmp5
//                + "." + tmp6 + "." + tmp7 + ".1-" + tmp8 + " ";
//        } else if ((tmp7 - tmp3) > 1) {
//            str = tmp1 + "." + tmp2 + "." + tmp3 + "." + tmp4 + "-255 ";
//            for (long n = (tmp3 + 1); n < tmp7; n++) {
//                str += tmp1 + "." + tmp2 + "." + n + ".1-255 ";
//            }
//            str += tmp5 + "." + tmp6 + "." + tmp7 + ".1-" + tmp8 + " ";
//        }
//        return str;
//    }

}
