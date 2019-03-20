package com.nanourl.logic.exceptions;

import javax.ejb.ApplicationException;

@ApplicationException(rollback=true)
public class EmailAlreadyInUseException extends Exception {}