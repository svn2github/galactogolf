package com.galactogolf.genericobjectmodel.levelloader;

import com.galactogolf.database.DatabaseException;

public class LevelLoadingException extends Exception{

	public LevelLoadingException(String message) {
		super(message);
	}

	public LevelLoadingException(String message, DatabaseException ex) {
		super(message,ex);
	}

}
