package com.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.text.TextUtils;

public class MD5Utils 
{
	/**
	    * �÷�����ָ�����ַ�����MD5�㷨���ܺ󷵻ء�
	    * @param s
	    * @return
	    */
	    public static String getMD5Encoding( String s ) 
	    {
	    	byte[] input= s.getBytes();
	    	String output = null;
	    	try
	    	{
	    		//  ���һ��MD5ժҪ�㷨�Ķ���
	    		MessageDigest md=MessageDigest.getInstance("MD5");
	    		md.update(input);
	    		// MD5�㷨�Ľ����128λһ��������������javaAPI�Ѿ��ѽ��ת�����ֽ�������
	    		byte[] tmp = md.digest();//���MD5��ժҪ���
	    		output = toHexString(tmp);
	    	}
	    	catch(NoSuchAlgorithmException e)
	    	{
	    		e.printStackTrace();
	    	}
	    	return output;
	    }

	    public static byte[] getByteMD5( byte[] bData ) {
	    	if (bData == null || bData.length <= 0)
	    		return null;
	    	byte[] bRes = null;
	    	try {
	    		MessageDigest md = MessageDigest.getInstance("MD5");
	    		md.reset();
				md.update(bData);
				bRes = md.digest();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				bRes = null;
			}
			return bRes;
	    }
	    
	    public static byte[] getFileMD5( String sFile ) {
	    	if (TextUtils.isEmpty(sFile))
	    		return null;
	    	byte[] bRes = null;
	    	try {
				FileInputStream fis = new FileInputStream(sFile);
				int nBytes = fis.available();
				int nBufSize = Math.min(10240, nBytes);
				byte[] bBuf = new byte[nBufSize];
	    		MessageDigest md = MessageDigest.getInstance("MD5");
	    		md.reset();
				for(int n = 0; n < nBytes; n += nBufSize) {
					int nRead = fis.read(bBuf);
					if (nRead <= 0)
						break;
					md.update(bBuf, 0, nRead);
				}
				fis.close();
				bRes = md.digest();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				bRes = null;
			}
			return bRes;
	    }

	    public static String toMd5( String s ) 
	    {
	    	byte[] bytes = s.getBytes();
            try 
            {
            	MessageDigest algorithm = MessageDigest.getInstance("MD5");
                algorithm.reset();
                algorithm.update(bytes);
                return toHexString(algorithm.digest(), "");
            } 
            catch(NoSuchAlgorithmException e)
            {
                throw new RuntimeException(e);      
            }
	    }
	    
	    public static String toHexString(byte[] bytes) {
	    	//  ����16������ĸ
	    	char[] hexChar={'0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f'};
    		char[] str = new char[32];
    		byte b = 0;
    		for( int i=0; i < 16; i++)
    		{
    			b = bytes[i];
    			str[2*i] = hexChar[(b >> 4) & 0xf];//ȡÿһ���ֽڵĸ���λ����16������ĸ
    			str[2*i+1] = hexChar[b & 0xf];//ȡÿһ���ֽڵĵ���λ����16������ĸ
    		}
    		return new String(str);
	    }

	    public static String toHexString(byte[] bytes, String separator) {
            StringBuilder hexString = new StringBuilder();
            for(byte b : bytes) 
            {
            	String s = Integer.toHexString(0xFF & b);
            	if (TextUtils.isEmpty(s))
            		s = "00";
            	else if (s.length() == 1)
            		s = "0" + s;
            	hexString.append(s).append(separator);
            }
            return hexString.toString();
	    }

}
