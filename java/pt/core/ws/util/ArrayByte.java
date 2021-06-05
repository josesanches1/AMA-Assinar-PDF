package pt.core.ws.util;

public class ArrayByte {

	/**
	 * Junta dois arrays
	 * 
	 * @param a
	 * @param b
	 * @return
	 */
	public byte[] juntar(byte[] a, byte[] b) {
		byte[] c = new byte[a.length + b.length];
		System.arraycopy(a, 0, c, 0, a.length );
		System.arraycopy(b, 0, c, a.length, b.length );
		return c;
	}
}
