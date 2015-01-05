<?php
function getTour(){
  $sql = "SELECT tb_tour.codigo, tb_tour.nome, tb_pernas.origem, tb_pernas.destino FROM tb_tour left join tb_pernas on tb_pernas.cod_tour=tb_tour.codigo WHERE tb_tour.status='Em Atividade' order by tb_tour.nome, tb_pernas.codigo";
  $result = mysql_query( $sql );
  if (!$result) {
    mysql_close();
    die("Error - Database Connection.");
  }
  $tour=0;
  $perna=1;
  echo "<list>";
  while($row = mysql_fetch_array($result)){
    if ($tour!=$row['codigo']){
		if ($tour!=0){
			echo "</pernas>";
			echo "</br.com.aeroboteco.model.Tour>";
		}
        $tour=$row['codigo'];
		$perna=1;
        echo "<br.com.aeroboteco.model.Tour>";
		echo "<codigo>".$tour."</codigo>";
		echo "<nome>".$row['nome']."</nome>";
		echo "<pernas>";
    }
    echo "<br.com.aeroboteco.model.PernaTour>";
	echo "<tour>".$tour."</tour>";
	echo "<sequencia>".$perna++."</sequencia>";
	echo "<origem>".$row['origem']."</origem>";
	echo "<destino>".$row['destino']."</destino>";
	echo "</br.com.aeroboteco.model.PernaTour>";
  }
  echo "</pernas>";
  echo "</br.com.aeroboteco.model.Tour>";
  echo "</list>";
}

function getApt($icao){
  $sql = "SELECT * FROM apt where icao='".$icao."'";
  $result = mysql_query( $sql );
  if (!$result) {
    mysql_close();
    die("Error - Database Connection.");
  }
  if($row = mysql_fetch_array($result)){
    echo $row['icao'].";".$row['nome'].";".$row['latitude'].";".$row['longitude'];
  }
}

function getPirepsEnviados($bto){
  	$sql = "SELECT codigo, cod_tour, origem, destino, status FROM tb_pireps_efetivo where bto='$bto'";
  	$result = mysql_query( $sql );
  	if (!$result) {
    	mysql_close();
    	die("Error - Database Connection.");
  	}
	while($row = mysql_fetch_array($result)){
		echo $row['codigo'].";".$row['cod_tour'].";".$row['origem'].";".$row['destino'].";".$row['status']."#";
  	}
}

mysql_connect("internal-db.s67203.gridserver.com", "db67203", "XkFs8y9t") or die("Unable to conneo host $hostName");
mysql_select_db("db67203_piloto") or die("Unable to select database piloto");

if ($_GET['tipo']=="apt"){
	getApt($_GET['icao']);
}else if ($_GET['tipo']=="pirepsenviados"){
	getPirepsEnviados($_GET['bto']);
}else{
	getTour();
}

mysql_close();

?>