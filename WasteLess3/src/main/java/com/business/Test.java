package com.business;

public class Test {
	
	public static void main(String[] args) {
		
		int[] a = {0,0,1,1,1,0,0,0,1,1,0,0,0,1,1,1,1,0,1,1,1,1,1,1};
		
		int maxNrOfConsecutiveOnes = 0;
		int currNumberOfConsecutiveOnes = 0;
		
		for(int i = 0; i < a.length; i ++) {
			
			if(a[i] == 1) {
				currNumberOfConsecutiveOnes ++;
				
				if(currNumberOfConsecutiveOnes > maxNrOfConsecutiveOnes) {
					maxNrOfConsecutiveOnes = currNumberOfConsecutiveOnes;
				}
			}else {
				currNumberOfConsecutiveOnes = 0;
			}
		}
		
		System.out.println("Maximum nr of consecutive"
				+ "ones : " + maxNrOfConsecutiveOnes);
	}
}
