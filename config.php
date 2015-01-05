<?php

$hostName = "internal-db.s67203.gridserver.com";
$dbName   = "db67203_va";
$userName = "db67203";
$password = "XkFs8y9t";

@define ("REPORT_FILE_URL",		"http://www.aeroboteco.com.br/va/fsacars/logs/");			// URL where the complete FSAcars reports will be stored
@define ("REPORT_FILE_PATH",		"/home/67203/domains/aeroboteco.com.br/html/va/fsacars/logs/");		// Folder where the complete FSAcars reports will be stored
@define ("ERROR_LOG_PATH",		"/home/67203/domains/aeroboteco.com.br/html/va/fsacars/logs/erros.log");	// Folder and filename where the error log is located

@define ("ERROR_OPENING_REPORT_FILE",	"Error opening report file");
@define ("ERROR_WRITING_REPORT_FILE",	"Error writing report file");
@define ("PILOT_NOT_FOUND",		"Pilot not found");
@define ("ERROR_IN_PILOT_QUERY",	"Pilot query error");
@define ("ERROR_INSERTING_PIREP",	"Error inserting report");

?>
