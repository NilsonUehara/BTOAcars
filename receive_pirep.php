<?php

include("../include/bd.php");
include("config.php");


function CheckFSAcarsInfo() {
	// Verify input
	if (!isset($_GET['pilot'])) { return 0; }
	if ($_GET['version']!='BTOACARS') { return 0; }

	// Request is not empty
	return 1;
}

function GetFSAcarsInfo() {
	/* ************************************************************************************************
	   @GetFSAcarsInfo
	   Receives inputs sent by FSAcars program and returns an array containing that information

	   Inputs: N/A
	   Outputs: string array
	   ************************************************************************************************ */

	// DO NOT EDIT THIS FUNCTION - THIS FIELDS ARE SENT BY FSACARS

        //Nilson 29/09/2009 alterei o campo date = $_GET['date'] pela data atual 
	//                  para evitar que usuários enviem a data errada
	$fsacars_pirep = array (
		"pilot" => $_GET['pilot'],
		"date" => date("Y/m/d"),
		"time" => $_GET['time'],
		"callsign" => $_GET['callsign'],
		"reg" => $_GET['reg'],
		"origin" => $_GET['origin'],
		"dest" => $_GET['dest'],
		"equipment" => $_GET['equipment'],
		"fuel" => $_GET['fuel'],
		"duration" => $_GET['duration'],
		"distance" => $_GET['distance'],
		"rep_url" => "Dummy",
		"tour" => $_GET['tour'],
		"more" => $_GET['more'],
		"fsacars_log" => $_GET['log']	// Get complete FSAcars log
	);

	/* DEBUG CODE - Write request to log file
	*/
	
	$fe = fopen (ERROR_LOG_PATH, "a");
	fwrite($fe, "[DEBUG ".date("d.m.y H:i:s")."] PILOT: ".$_GET['pilot']." DATE: ".$_GET['date']." TIME: ".$_GET['time']." CALLSIGN: ".$_GET['callsign']." REG: ".$_GET['reg']." ORIG: ".$_GET['origin']." DEST: ".$_GET['dest']." EQUIP: ".$_GET['equipment']." FUEL: ".$_GET['fuel']." DURATION: ".$_GET['duration']." DIST: ".$_GET['distance']." MORE: ".$_GET['more']." LOG: ".$_GET['log']."\n");
	fclose($fe);

	return $fsacars_pirep;
}

function SavePIREPFile($pirep_array) {
	/* ************************************************************************************************
	   @SavePIREPFile
	   Receives a string array with FSAcars pireps and creates or appends information to pirep file

	   Inputs: string array
	   Outputs: 1 sucess, 0 error
	   ************************************************************************************************ */

	/* Build report filename and URL */
	$filename=$pirep_array['pilot'].str_replace("/","",$pirep_array['date']).str_replace(":","",$pirep_array['time']).".txt";
	$pirep_array['rep_url']=REPORT_FILE_URL.$pirep_array['pilot']."/".$filename;

	/* Parse FsAcars log */
	$fsacars_log_lines_array = explode("*",$pirep_array['fsacars_log']);

	/* Create or Append FSAcars report file */

	$fp = fopen (REPORT_FILE_PATH.$pirep_array['pilot']."/".$filename, "a");

	if (!$fp) {
		/* Error opening file */
		$fe = fopen (ERROR_LOG_PATH, "a");
		fwrite($fe, "[ERROR ".date("d.m.y H:i:s")."] PILOT: ".$pirep_array['pilot']." - ".$ERROR_OPENING_REPORT_FILE." - ".$filename."\n");
		fclose($fe);

		return 0;
	}

	/*
	* Write all log lines received from FSAcars */
	for($i=0;$i<count($fsacars_log_lines_array);$i++) {
    		if (!fwrite($fp, $fsacars_log_lines_array[$i] . "\n")) {
        		/* Error writing to file */
        		$fe = fopen (ERROR_LOG_PATH, "a");
			fwrite($fe, "[ERROR ".date("d.m.y H:i:s")."] PILOT".$pirep_array['pilot']." - ".$ERROR_WRITING_REPORT_FILE." - ".$filename."\n");
			fclose($fe);

        		return 0;
    		}
	}

	/* Close file */
	fclose($fp);

	return 1;
}

function InsertReportIntoDB($pirep_array) {
	/* ************************************************************************************************
	   @InsertReportIntoDB
	   Receives a string array with FSAcars pireps and inserts summary into reports table

	   Inputs: string array
	   Outputs: 1 sucess, 0 error
	   ************************************************************************************************ */

	/* If this is the first chunk insert PIREP on database */
	if ($pirep_array['more']=="0") {
    		/*
    		 * Verify pilot identity (From VA Pilots table) */
    		$the_pilot = $pirep_array['pilot'];
    		$stmt = "select id from pilotos where id='$the_pilot'";
    		$result = mysql_query($stmt);

    		/* mysql error */
    		if (!$result) {
    			$fe = fopen (ERROR_LOG_PATH, "a");
				fwrite($fe, "[ERROR ".date("d.m.y H:i:s")."] ".ERROR_IN_PILOT_QUERY." - Pilot ".$pirep_array['pilot']." - ".mysql_error()." SQL: ".$stmt."\n");
				fclose($fe);

				return 0;
    		}

    		if (mysql_num_rows($result) == 0) {
       			/* Pilot not found */
       			$fe = fopen (ERROR_LOG_PATH, "a");
				fwrite($fe, "[ERROR ".date("d.m.y H:i:s")."] ".PILOT_NOT_FOUND." - Pilot ".$pirep_array['pilot']."\n");
				fclose($fe);
				return 0;
    		} else {
          			/* Pilot found */
          			$pilot_id = mysql_result($result,0,"id");
					$pilot_id = substr($pilot_id,3);

          			/* Insert info on reports table */
          			$values = $pilot_id.",'".$pirep_array['date']."','".$pirep_array['time']."','".$pirep_array['callsign']."','".$pirep_array['origin']."','".$pirep_array['dest']."','".$pirep_array['reg']."','".$pirep_array['equipment']."','".$pirep_array['duration']."',".$pirep_array['fuel'].",".$pirep_array['distance'].",'".$pirep_array['rep_url']."'";
          			$stmt = "INSERT INTO reports (pilot_id,date,time,callsign,origin_id,destination_id,registration,equipment,duration,fuel,distance,fsacars_rep_url) VALUES ($values)";
          			$result = mysql_query($stmt);

          			if (!$result) {
    					$fe = fopen (ERROR_LOG_PATH, "a");
						fwrite($fe, "[ERROR ".date("d.m.y H:i:s")."] ".ERROR_INSERTING_PIREP." - Pilot ".$pirep_array['pilot']." - ".mysql_error()." SQL: ".$stmt."\n");
						fclose($fe);
						return 0;
    				}
					$t_cod_tour = $pirep_array['tour'];
            	    $t_piloto = $pirep_array['pilot'];
                	$t_origem = $pirep_array['origin'];
	                $t_destino = $pirep_array['dest'];
    	            $t_tempo = $pirep_array['duration'];
        	        $t_distancia = $pirep_array['distance'];
            	    $t_link = $pirep_array['rep_url'];

					if ($t_cod_tour != 0) {

	                    $d_agora = date("Ymd");
    	                $h_agora = date("H:i");

                        $insere = "INSERT INTO tb_pireps_efetivo (bto, cod_tour, origem, destino, status, data, hora, log, tempo, distancia, link) VALUES ('".$t_piloto."', '".$t_cod_tour."', '".$t_origem."', '".$t_destino."', 'Aguardando Aprovação', '".$d_agora."', '".$h_agora."', '".$t_log."', '".$t_tempo."', '".$t_distancia."', '".$t_link."')";

          				$executa = mysql_query($insere);
                	}
				//----------------------
				
				
				$hora=$pirep_array['duration'];
				$tempo=(substr($hora,0,2)*60+substr($hora,3,2))/60;
	       		$stmt = "update pilotos set horas=horas+$tempo where id='".$pirep_array['pilot']."'";
       			$result = mysql_query($stmt);

				//Baixar vôo da VA
				if ($pirep_array['origin'] != "") {
  				  if ($pirep_array['dest'] != "") {
				    $stmt="select * from pilotos where id='".$pirep_array['pilot']."' and origem='".strtoupper($pirep_array['origin'])."' and destino='".strtoupper($pirep_array['dest'])."'";
       			    $resultva = mysql_query($stmt);
				    if (!$resultva) {
				      //Não é o trecho selecionado no plano de vôo
				    } else {
				      $arr = mysql_fetch_assoc($resultva);
  				      if ($arr['id'] == $pirep_array['pilot']) {
				        if ($arr['origem'] == strtoupper($pirep_array['origin'])) {
				          if ($arr['destino'] == strtoupper($pirep_array['dest'])) {
					    //liberar novo PV
				            $stmt="update pilotos set ultpv=concat(origem,' -> ', destino, ' Via: ',rota), origem=destino, destino='', rota='' where id='".$pirep_array['pilot']."' and origem='".strtoupper($pirep_array['origin'])."' and destino='".strtoupper($pirep_array['dest'])."'";
	        			    $resultva = mysql_query($stmt);
						    //Gravar o trecho na tabela de vôos efetuados
						    $stmt="insert into voos (id,data,origem,destino,duracao) values ('".$pirep_array['pilot']."','".$pirep_array['date']."','".strtoupper($pirep_array['origin'])."','".strtoupper($pirep_array['dest'])."',".$tempo.")";
	       			  	    $resultva = mysql_query($stmt);
				         }
				        }
				      }
				    }
				  }
		        }
    		}
    	}

    	return 1;
}

function main() {
	/* ************************************************************************************************
	   @main

	   Inputs: N/A
	   Outputs: "OK" sucess, "NOTOK" error
	   ************************************************************************************************ */

	$res = CheckFSAcarsInfo();
	if ($res == 0) {
		return "NOTOK 1";
	}

	$a = GetFSAcarsInfo();

	$res = SavePIREPFile(&$a);
	if ($res == 0) {
		return "NOTOK 2";
	}

	$res = InsertReportIntoDB($a);
	if ($res == 0) {
		return "NOTOK 3";
	}

	// Report sucessfully received
	return "OK";
}

/* receive_pirep.php return to FSACARS */
$out = main();
echo $out;

?>

