package com.neusoft.soc.nli.jamass.util;

import java.net.InetAddress;
import java.net.UnknownHostException;

// import java.util.Arrays;

/*
 * Copyright 2004-2005 Sun Microsystems, Inc.  All Rights Reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Sun designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Sun in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Sun Microsystems, Inc., 4150 Network Circle, Santa Clara,
 * CA 95054 USA or visit www.sun.com if you need additional information or
 * have any questions.
 */

public class InetV6AddressUtil {
    private final static int INADDR4SZ = 4;
    private final static int INADDR16SZ = 16;
    private final static int INT16SZ = 2;

    /*
     * Converts IPv4 address in its textual presentation form into its numeric
     * binary form.
     * 
     * @param src a String representing an IPv4 address in standard format
     * 
     * @return a byte array representing the IPv4 numeric address
     */
    public static byte[] textToNumericFormatV4(String src) {
        if (src.length() == 0) {
            return null;
        }

        byte[] res = new byte[INADDR4SZ];
        String[] s = src.split("\\.", -1);
        long val;
        try {
            switch (s.length) {
            case 1:
                /*
                 * When only one part is given, the value is stored directly in
                 * the network address without any byte rearrangement.
                 */

                val = Long.parseLong(s[0]);
                if (val < 0 || val > 0xffffffffL)
                    return null;
                res[0] = (byte) ((val >> 24) & 0xff);
                res[1] = (byte) (((val & 0xffffff) >> 16) & 0xff);
                res[2] = (byte) (((val & 0xffff) >> 8) & 0xff);
                res[3] = (byte) (val & 0xff);
                break;
            case 2:
                /*
                 * When a two part address is supplied, the last part is
                 * interpreted as a 24-bit quantity and placed in the right most
                 * three bytes of the network address. This makes the two part
                 * address format convenient for specifying Class A network
                 * addresses as net.host.
                 */

                val = Integer.parseInt(s[0]);
                if (val < 0 || val > 0xff)
                    return null;
                res[0] = (byte) (val & 0xff);
                val = Integer.parseInt(s[1]);
                if (val < 0 || val > 0xffffff)
                    return null;
                res[1] = (byte) ((val >> 16) & 0xff);
                res[2] = (byte) (((val & 0xffff) >> 8) & 0xff);
                res[3] = (byte) (val & 0xff);
                break;
            case 3:
                /*
                 * When a three part address is specified, the last part is
                 * interpreted as a 16-bit quantity and placed in the right most
                 * two bytes of the network address. This makes the three part
                 * address format convenient for specifying Class B net- work
                 * addresses as 128.net.host.
                 */
                for (int i = 0; i < 2; i++) {
                    val = Integer.parseInt(s[i]);
                    if (val < 0 || val > 0xff)
                        return null;
                    res[i] = (byte) (val & 0xff);
                }
                val = Integer.parseInt(s[2]);
                if (val < 0 || val > 0xffff)
                    return null;
                res[2] = (byte) ((val >> 8) & 0xff);
                res[3] = (byte) (val & 0xff);
                break;
            case 4:
                /*
                 * When four parts are specified, each is interpreted as a byte
                 * of data and assigned, from left to right, to the four bytes
                 * of an IPv4 address.
                 */
                for (int i = 0; i < 4; i++) {
                    val = Integer.parseInt(s[i]);
                    if (val < 0 || val > 0xff)
                        return null;
                    res[i] = (byte) (val & 0xff);
                }
                break;
            default:
                return null;
            }
        } catch (NumberFormatException e) {
            return null;
        }
        return res;
    }

    /*
     * Convert IPv6 presentation level address to network order binary form.
     * credit: Converted from C code from Solaris 8 (inet_pton)
     * 
     * Any component of the string following a per-cent % is ignored.
     * 
     * @param src a String representing an IPv6 address in textual format
     * 
     * @return a byte array representing the IPv6 numeric address
     */
    public static byte[] textToNumericFormatV6(String src) {
        // Shortest valid string is "::", hence at least 2 chars
        if (src.length() < 2) {
            return null;
        }

        int colonp;
        char ch;
        boolean saw_xdigit;
        int val;
        char[] srcb = src.toCharArray();
        byte[] dst = new byte[INADDR16SZ];

        int srcb_length = srcb.length;
        int pc = src.indexOf("%");
        // %���������zoneID
        if (pc == srcb_length - 1) {
            return null;
        }

        if (pc != -1) {
            srcb_length = pc;
        }

        colonp = -1;
        int i = 0, j = 0;
        /* Leading :: requires some special handling. */
        // i==0
        if (srcb[i] == ':')
            if (srcb[++i] != ':')
                return null;

        int curtok = i;
        saw_xdigit = false;
        val = 0;
        while (i < srcb_length) {
            ch = srcb[i++];
            int chval = Character.digit(ch, 16);
            if (chval != -1) {
                val <<= 4;
                val |= chval;
                if (val > 0xffff)
                    // must <= 0xffff
                    return null;
                saw_xdigit = true;
                continue;
            }
            if (ch == ':') {
                curtok = i;
                if (!saw_xdigit) {
                    if (colonp != -1)
                        return null;
                    colonp = j;
                    continue;
                } else if (i == srcb_length) {
                    return null;
                }
                if (j + INT16SZ > INADDR16SZ)
                    return null;
                dst[j++] = (byte) ((val >> 8) & 0xff);
                dst[j++] = (byte) (val & 0xff);
                saw_xdigit = false;
                val = 0;
                continue;
            }
            if (ch == '.' && ((j + INADDR4SZ) <= INADDR16SZ)) {
                String ia4 = src.substring(curtok, srcb_length);
                /* check this IPv4 address has 3 dots, ie. A.B.C.D */
                int dot_count = 0, index = 0;
                while ((index = ia4.indexOf('.', index)) != -1) {
                    dot_count++;
                    index++;
                }
                if (dot_count != 3) {
                    return null;
                }
                byte[] v4addr = textToNumericFormatV4(ia4);
                if (v4addr == null) {
                    return null;
                }
                for (int k = 0; k < INADDR4SZ; k++) {
                    dst[j++] = v4addr[k];
                }
                saw_xdigit = false;
                break; /* '\0' was seen by inet_pton4(). */
            }
            return null;
        }
        if (saw_xdigit) {
            if (j + INT16SZ > INADDR16SZ)
                return null;
            dst[j++] = (byte) ((val >> 8) & 0xff);
            dst[j++] = (byte) (val & 0xff);
        }

        if (colonp != -1) {
            int n = j - colonp;

            if (j == INADDR16SZ)
                return null;
            for (i = 1; i <= n; i++) {
                dst[INADDR16SZ - i] = dst[colonp + n - i];
                dst[colonp + n - i] = 0;
            }
            j = INADDR16SZ;
        }
        if (j != INADDR16SZ)
            return null;
        byte[] newdst = convertFromIPv4MappedAddress(dst);
        if (newdst != null) {
            return newdst;
        } else {
            return dst;
        }
    }

    /**
     * @param src
     *            a String representing an IPv4 address in textual format
     * @return a boolean indicating whether src is an IPv4 literal address
     */
    public static boolean isIPv4LiteralAddress(String src) {
        return textToNumericFormatV4(src) != null;
    }

    /**
     * @param src
     *            a String representing an IPv6 address in textual format
     * @return a boolean indicating whether src is an IPv6 literal address
     */
    public static boolean isIPv6LiteralAddress(String src) {
        return textToNumericFormatV6(src) != null;
    }

    /*
     * Convert IPv4-Mapped address to IPv4 address. Both input and returned
     * value are in network order binary form.
     * 
     * @param src a String representing an IPv4-Mapped address in textual format
     * 
     * @return a byte array representing the IPv4 numeric address
     */
    public static byte[] convertFromIPv4MappedAddress(byte[] addr) {
        if (isIPv4MappedAddress(addr)) {
            byte[] newAddr = new byte[INADDR4SZ];
            System.arraycopy(addr, 12, newAddr, 0, INADDR4SZ);
            return newAddr;
        }
        return null;
    }

    /**
     * Utility routine to check if the InetAddress is an IPv4 mapped IPv6
     * address.(���::ffff:192.168.89.9��::0000:192.168.89.9)
     * 
     * @return a <code>boolean</code> indicating if the InetAddress is an IPv4
     *         mapped IPv6 address; or false if address is IPv4 address.
     */
    private static boolean isIPv4MappedAddress(byte[] addr) {
        if (addr.length < INADDR16SZ) {
            return false;
        }
        // ���Ӷ�::0000:192.168.89.9�ж�
        if ((addr[0] == 0x00)
            && (addr[1] == 0x00)
            && (addr[2] == 0x00)
            && (addr[3] == 0x00)
            && (addr[4] == 0x00)
            && (addr[5] == 0x00)
            && (addr[6] == 0x00)
            && (addr[7] == 0x00)
            && (addr[8] == 0x00)
            && (addr[9] == 0x00)
            && (((addr[10] == (byte) 0xff) && (addr[11] == (byte) 0xff)) || ((addr[10] == (byte) 0x00) && (addr[11] == (byte) 0x00)))) {
            return true;
        }
        return false;
    }

    /**
     * ��ʽ��IPV6
     * 
     * @param ipV6
     *            ipV6�ֽ�
     * @return ipV6�淶�ַ���
     */
    public static String normalizeIPV6(byte[] ipV6) {
        if (ipV6 == null || ipV6.length != 16) {
            return null;
        }
        StringBuilder builder = new StringBuilder();
        for (int i = 1; i <= ipV6.length; i++) {
            byte d = ipV6[i - 1];
            if (d < 0) {
                builder.append(Integer.toHexString(d + 256));
            } else if (d >= 0 && d <= 0xf) {
                builder.append("0").append(Integer.toHexString(d));
            } else {
                builder.append(Integer.toHexString(d));
            }
            if (i % 2 == 0 && i != (ipV6.length)) {
                builder.append(":");
            }
        }
        return builder.toString();
    }

    /**
     * ���ַ���IPת���ֽ�����IP,�÷������ܽϺ�
     * 
     * @param ip
     *            IPV4��ַ
     * @return �ֽ�����
     */
    public static byte[] getIpBytes(String ip) {
        byte[] bytes = new byte[4];
        int pos = ip.indexOf(".");
        bytes[0] = (byte) (0x0FF & Integer.parseInt(ip.substring(0, pos)));
        int nextpos = ip.indexOf(".", pos + 1);
        bytes[1] = (byte) (0x0FF & Integer.parseInt(ip.substring(pos + 1,
            nextpos)));
        pos = nextpos;
        nextpos = ip.indexOf(".", pos + 1);
        bytes[2] = (byte) (0x0FF & Integer.parseInt(ip.substring(pos + 1,
            nextpos)));
        bytes[3] = (byte) (0x0FF & Integer.parseInt(ip.substring(nextpos + 1,
            ip.length())));
        return bytes;
    }

    /**
     * ��IPV6
     * 
     * @param ipV6
     *            ipV6�ֽ�
     * @return ���ַ���
     */
    public static String shrinkIpV6(byte[] ipV6) {
        if (ipV6 == null || ipV6.length != 16) {
            return null;
        }
        StringBuilder builder = new StringBuilder();
        for (int i = 1; i <= ipV6.length; i++) {
            byte d = ipV6[i - 1];
            if (d < 0) {
                builder.append(Integer.toHexString(d + 256));
            } else if (d != 0) {
                builder.append(Integer.toHexString(d));
            }
            if (i % 2 == 0 && i != (ipV6.length)) {
                builder.append(":");
            }
        }
        return builder.toString();
    }

    public static void main(String[] args) {
        byte[] a = textToNumericFormatV6("fe80:d::230:48ff:fe30:7e40");
        System.out.println(normalizeIPV6(a));
        System.out.println(shrinkIpV6(a));
        System.out
            .println(textToNumericFormatV6("fe80::230:48ff:fe30:7e40").length);
        try {
            System.out.println(InetAddress.getByName(
                "fe80::230:48ff:fe30:7e40%34").getHostAddress());
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        // perf test
        long cur = System.nanoTime();
        for (int i = 0; i < 10000000; i++) {
            textToNumericFormatV4("192.168.140.40");
        }
        long end = System.nanoTime();
        System.out.println("cost -> " + (double) (end - cur)
            / (1000 * 1000 * 1000) + "s");
        // try {
        // Thread.sleep(1000*60);
        // } catch (InterruptedException e) {
        // e.printStackTrace();
        // }
        cur = System.nanoTime();
        for (int i = 0; i < 1000000; i++) {
            getIpBytes("192.168.140.40");
        }
        end = System.nanoTime();
        System.out.println("cost -> " + (double) (end - cur)
            / (1000 * 1000 * 1000) + "s");
    }
}
