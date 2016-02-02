package com.with.tourbuild;

public class Message implements Comparable<Message> {


	private String mFrom;
	private String mTo;
	private String mMessage;
	private Boolean mRead;
	private String mObjectId;

	public String getmObjectId() {
		return mObjectId;
	}
	public void setmObjectId(String mObjectId) {
		this.mObjectId = mObjectId;
	}
	public String getmFrom() {
		return mFrom;
	}
	public void setmFrom(String mFrom) {
		this.mFrom = mFrom;
	}
	public String getmTo() {
		return mTo;
	}
	public void setmTo(String mTo) {
		this.mTo = mTo;
	}
	public String getmMessage() {
		return mMessage;
	}
	public void setmMessage(String mMessage) {
		this.mMessage = mMessage;
	}
	public Boolean getmRead() {
		return mRead;
	}
	public void setmRead(Boolean mRead) {
		this.mRead = mRead;
	}

	public Message(String mFrom, String mTo, String mMessage, Boolean mRead,
				   String mObjectId) {
		super();
		this.mFrom = mFrom;
		this.mTo = mTo;
		this.mMessage = mMessage;
		this.mRead = mRead;
		this.mObjectId = mObjectId;
	}
	public Message(String mMessage) {
		super();
		this.mMessage = mMessage;

	}
	@Override
	public int compareTo(Message another) {

		return this.mFrom.compareTo(another.mFrom);
	}
	
}
