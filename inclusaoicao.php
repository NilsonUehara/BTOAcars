<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>BTOAcars - Inclusão de ICAO</title>
<style type="text/css">
<!--
.obs {
	color: #F00;
}
.titulo {
	font-weight: bold;
}
-->
</style>
</head>

<body>
<?php
if ($_GET['acao']=="gravar"){
	mysql_connect("internal-db.s67203.gridserver.com", "db67203", "XkFs8y9t") or die("Unable to conneo host $hostName");
	mysql_select_db("db67203_piloto") or die("Unable to select database piloto");
	
	$sql = "replace into apt (icao,nome,latitude,longitude) values ('$txtICAO','$txtNome',$txtLat,$txtLon)";
	$result = mysql_query( $sql );
	if (!$result) {
    	mysql_close();
		die("Error - Database Connection.");
  	}else{
		echo "<h1>ICAO:$txtICAO incluído com sucesso!</h1><br/>";
	}
	
	mysql_close();
}
?>

<p class="titulo">BTOAcars: Inclusão de ICAO</p>
<form id="form" action="./inclusaoicao.php">
<input name="acao" type="hidden" id="acao" value="gravar" />
<table width="500" border="0">
  <tr>
    <td>ICAO:</td>
    <td><input name="txtICAO" type="text" id="txtICAO" size="4" maxlength="4" /> 
      (SBMT)</td>
  </tr>
  <tr>
    <td>Nome:</td>
    <td><input name="txtNome" type="text" id="txtNome" size="30" maxlength="50" /> 
      (Campo de Marte)</td>
  </tr>
  <tr>
    <td>Latitude:</td>
    <td><input name="txtLat" type="text" id="txtLat" size="13" maxlength="13" /> 
      (-23.509119)</td>
  </tr>
  <tr>
    <td>Longitude:</td>
    <td><input name="txtLon" type="text" id="txtLong" size="13" maxlength="13" /> 
      (-46.637753)</td>
  </tr>
  <tr>
    <td colspan="2" align="center"><input type="submit" name="btGravar" id="btGravar" value="Gravar" /></td>
  </tr>
</table>
</form>
<p class="obs">Obs: Use . (ponto) para separar a parte decimal de latitude e longitude</p>
</body>
</html>