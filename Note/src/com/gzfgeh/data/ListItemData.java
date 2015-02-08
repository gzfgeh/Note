package com.gzfgeh.data;

public class ListItemData {
	private int _id;
	private String content;
	private String contentUri;
	private String date;
	private int isAlarm;
	private long encrypt;
	private String ringName;
	private String ringDate;
	private String ringUri;
	
	public ListItemData(int _id, String content, String contentUri, String date,
			int isAlarm, long encrypt, String ringName, String ringDate,
			String ringUri) {
		super();
		this._id = _id;
		this.content = content;
		this.contentUri = contentUri;
		this.date = date;
		this.isAlarm = isAlarm;
		this.encrypt = encrypt;
		this.ringName = ringName;
		this.ringDate = ringDate;
		this.ringUri = ringUri;
	}
	
	
	public int get_id() {
		return _id;
	}


	public void set_id(int _id) {
		this._id = _id;
	}


	public String getContent() {
		return content;
	}


	public void setContent(String content) {
		this.content = content;
	}


	public String getContentUri() {
		return contentUri;
	}


	public void setContentUri(String contentUri) {
		this.contentUri = contentUri;
	}


	public String getDate() {
		return date;
	}


	public void setDate(String date) {
		this.date = date;
	}


	public int getIsAlarm() {
		return isAlarm;
	}


	public void setIsAlarm(int isAlarm) {
		this.isAlarm = isAlarm;
	}


	public long getEncrypt() {
		return encrypt;
	}


	public void setEncrypt(long encrypt) {
		this.encrypt = encrypt;
	}


	public String getRingName() {
		return ringName;
	}


	public void setRingName(String ringName) {
		this.ringName = ringName;
	}


	public String getRingDate() {
		return ringDate;
	}


	public void setRingDate(String ringDate) {
		this.ringDate = ringDate;
	}


	public String getRingUri() {
		return ringUri;
	}


	public void setRingUri(String ringUri) {
		this.ringUri = ringUri;
	}


	@Override
	public String toString() {
		return "ListItemData [_id=" + _id + ", content=" + content
				+ ", contentUri=" + contentUri + ", date=" + date
				+ ", isAlarm=" + isAlarm + ", encrypt=" + encrypt
				+ ", ringName=" + ringName + ", ringDate=" + ringDate
				+ ", ringUri=" + ringUri + "]";
	}
}
