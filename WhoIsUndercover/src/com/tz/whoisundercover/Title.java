package com.tz.whoisundercover;

public class Title {
	private String titleBeg;
	private int total = 0;
	private int cur = 0;
	
	public Title(String titleBeg, int total) {
		this.titleBeg = titleBeg;
		this.total = total;
	}

	@Override
	public String toString() {
		return titleBeg + "(" + cur + "/"+ total + ")";
	}

	public void addVote() {
		if (cur >= total){
			return ; 
		}
		++ cur;
	}

	public int getCurVote() {
		return cur;
	}

	public int getTotalVote() {
		return total;
	}

	public void clearAllVote() {
		cur = 0;
	}

	public void kill() {
		-- total;
	}

}
