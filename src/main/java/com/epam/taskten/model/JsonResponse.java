package com.epam.taskten.model;

import java.io.Serializable;

public class JsonResponse implements Serializable
{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8951996786224665480L;

	private String status = null;
	private Object result = null;
	private String recordName = null;

	public String getStatus() 
	{
		return status;
	}

	public void setStatus(String status) 
	{
		this.status = status;
	}

	public Object getResult() 
	{
		return result;
	}

	public void setResult(Object result)
	{
		this.result = result;
	}

	public String getRecordName() 
	{
		return recordName;
	}

	public void setRecordName(String recordName)
	{
		this.recordName = recordName;
	}

	@Override
	public String toString()
	{
		return "JsonResponse{" +
				"status='" + status + '\'' +
				", result=" + result + '\'' +
				", recordName=" + recordName +
				'}';
	}

	@Override
	public boolean equals(Object o)
	{
		if (this == o)
		{
			return true;
		}
		if (o == null || getClass() != o.getClass())
		{
			return false;
		}

		JsonResponse that = (JsonResponse) o;

		if (recordName != null ? !recordName.equals(that.recordName) : that.recordName != null) 
		{
			return false;			
		}
		if (result != null ? !result.equals(that.result) : that.result != null)
		{
			return false;
		}
		if (status != null ? !status.equals(that.status) : that.status != null)
		{
			return false;
		}

		return true;
	}

	@Override
	public int hashCode()
	{
		int result1 = status != null ? status.hashCode() : 0;
		result1 = 31 * result1 + (result != null ? result.hashCode() : 0);
		result1 = 31 * result1 + (recordName != null ? recordName.hashCode() : 0);
		return result1;
	}
}
