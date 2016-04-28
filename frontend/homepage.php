<?php
	if(isset($_GET['submit'])){
	// Fetching variables of the form which travels in URL
	$query = $_GET['query'];
	if($query !='')
	{
header("Location:http://www.formget.com/app/");
	}
	else{
	}
	}
	echo "string";
?>