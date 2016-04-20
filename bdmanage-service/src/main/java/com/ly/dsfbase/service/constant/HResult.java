package com.ly.dsfbase.service.constant;

/*
 * The Result of DAO Service API execute
 */
public class HResult<T> {
	private int errcode;
	private String errmsg;
    private String offset;

	private T data;

	public static HResult DEFAULT_OK = new HResult(0, "");

    public HResult(){
        this.errcode = 0;
        this.errmsg = "";
    }
	public HResult(int errcode, String errmsg) {
		this.errcode = errcode;
		this.errmsg = errmsg == null ? "" : errmsg;
	}

    public HResult(int errcode, String errmsg,String offset) {
        this.errcode = errcode;
        this.errmsg = errmsg == null ? "" : errmsg;
        this.offset = offset;
    }




    public HResult(int errcode, String errmsg, T data) {
		this.errcode = errcode;
		this.errmsg = errmsg == null ? "" : errmsg;
		this.data = data;
	}


    public int getErrcode() {
        return errcode;
    }

    public void setErrcode(int errcode) {
        this.errcode = errcode;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getOffset() {
        return offset;
    }

    public void setOffset(String offset) {
        this.offset = offset;
    }
}
