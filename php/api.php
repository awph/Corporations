<?php
	$debug = false;
	if($debug) {
		error_reporting(E_ALL);
		ini_set("display_errors", 1);
	}

	$username = '';
	$password = '';
	$host = '';
	$database_name = 'corporations';

	// Structured array for create the query
	$whats = array(
		'leaderboard' => array(
			'name' => 'leaderboard',
			'parameters' => array(
				'identifier' => array(
					'value' => null,
					'type' => PDO::PARAM_STR
				),
				'start' => array(
					'value' => null,
					'type' => PDO::PARAM_INT
				),
				'limit' => array(
					'value' => null,
					'type' => PDO::PARAM_INT
				),
			),
			'array' => true,
			'nullResult' => false,
		),
		'profile' => array(
			'name' => 'profile_info',
			'parameters' => array(
				'identifier' => array(
					'value' => null,
					'type' => PDO::PARAM_STR
				),
			),
			'array' => false,
			'nullResult' => false,
		),
		'trips' => array(
			'name' => 'trips',
			'parameters' => array(
				'identifier' => array(
					'value' => null,
					'type' => PDO::PARAM_STR
				),
				'limit' => array(
					'value' => null,
					'type' => PDO::PARAM_INT
				),
			),
			'array' => true,
			'nullResult' => false,
		),
		'territories' => array(
			'name' => 'territories_for_location',
			'parameters' => array(
				'identifier' => array(
					'value' => null,
					'type' => PDO::PARAM_STR
				),
				'lat' => array(
					'value' => null,
					'type' => PDO::PARAM_STR
				),
				'lng' => array(
					'value' => null,
					'type' => PDO::PARAM_STR
				),
				'limit' => array(
					'value' => null,
					'type' => PDO::PARAM_INT
				),
			),
			'array' => true,
			'nullResult' => false,
		),
		'purchaseTerritory' => array(
			'name' => 'purchase_territory',
			'parameters' => array(
				'identifier' => array(
					'value' => null,
					'type' => PDO::PARAM_STR
				),
				'lat' => array(
					'value' => null,
					'type' => PDO::PARAM_STR
				),
				'lng' => array(
					'value' => null,
					'type' => PDO::PARAM_STR
				),
				'owner' => array(
					'value' => null,
					'type' => PDO::PARAM_INT
				),
				'price' => array(
					'value' => null,
					'type' => PDO::PARAM_INT
				),
			),
			'array' => false,
			'nullResult' => false,
		),
		'captureTerritory' => array(
			'name' => 'capture_special_territory',
			'parameters' => array(
				'identifier' => array(
					'value' => null,
					'type' => PDO::PARAM_STR
				),
				'lat' => array(
					'value' => null,
					'type' => PDO::PARAM_STR
				),
				'lng' => array(
					'value' => null,
					'type' => PDO::PARAM_STR
				),
				'owner' => array(
					'value' => null,
					'type' => PDO::PARAM_INT
				),
			),
			'array' => false,
			'nullResult' => false,
		),
		'connection' => array(
			'name' => 'create_or_update_player',
			'parameters' => array(
				'identifier' => array(
					'value' => null,
					'type' => PDO::PARAM_STR
				),
				'user_id' => array(
					'value' => null,
					'type' => PDO::PARAM_STR
				),
				'token' => array(
					'value' => null,
					'type' => PDO::PARAM_STR
				),
				'lat' => array(
					'value' => null,
					'type' => PDO::PARAM_STR
				),
				'lng' => array(
					'value' => null,
					'type' => PDO::PARAM_STR
				),
			),
			'array' => false,
			'nullResult' => false,
		),
		'updateAlliance' => array(
			'name' => 'update_alliance',
			'parameters' => array(
				'identifier' => array(
					'value' => null,
					'type' => PDO::PARAM_STR
				),
				'ally' => array(
					'value' => null,
					'type' => PDO::PARAM_STR
				),
				'createOrDelete' => array(
					'value' => null,
					'type' => PDO::PARAM_INT
				),
			),
			'array' => false,
			'nullResult' => true,
		),
		'uploadTrip' => array(
			'name' => 'upload_trip',
			'parameters' => array(
				'identifier' => array(
					'value' => null,
					'type' => PDO::PARAM_STR
				),
				'distance' => array(
					'value' => null,
					'type' => PDO::PARAM_INT
				),
				'secondes' => array(
					'value' => null,
					'type' => PDO::PARAM_INT
				),
				'date' => array(
					'value' => null,
					'type' => PDO::PARAM_STR
				),
			),
			'array' => false,
			'nullResult' => true,
		),
		'updateProfile' => array(
			'name' => 'update_profile',
			'parameters' => array(
				'identifier' => array(
					'value' => null,
					'type' => PDO::PARAM_STR
				),
				'experiencePointsPrice' => array(
					'value' => null,
					'type' => PDO::PARAM_INT
				),
				'purchasePriceSkillLevel' => array(
					'value' => null,
					'type' => PDO::PARAM_INT
				),
				'purchaseDistanceSkillLevel' => array(
					'value' => null,
					'type' => PDO::PARAM_INT
				),
				'experienceLimitSkillLevel' => array(
					'value' => null,
					'type' => PDO::PARAM_INT
				),
				'moneyLimitSkillLevel' => array(
					'value' => null,
					'type' => PDO::PARAM_INT
				),
				'experienceQuantityFoundSkillLevel' => array(
					'value' => null,
					'type' => PDO::PARAM_INT
				),
				'alliancePriceSkillLevel' => array(
					'value' => null,
					'type' => PDO::PARAM_INT
				),
			),
			'array' => false,
			'nullResult' => false,
		),
		'changePrice' => array(
			'name' => 'change_territory_price',
			'parameters' => array(
				'identifier' => array(
					'value' => null,
					'type' => PDO::PARAM_STR
				),
				'newPrice' => array(
					'value' => null,
					'type' => PDO::PARAM_INT
				),
				'lat' => array(
					'value' => null,
					'type' => PDO::PARAM_STR
				),
				'lng' => array(
					'value' => null,
					'type' => PDO::PARAM_STR
				),
			),
			'array' => false,
			'nullResult' => false,
		),
	);

	$availableStatus = array(
		'OK',					// 0
		'UNKNOW_IDENTIFIER',	// 1
		'UNKNOW_REQUEST',		// 2
		'INVALID_PARAMETER',	// 3
		'UNKNOWN_ERROR',		// 4
		'OWNER_CHANGE',			// 5
		'NOT_ENOUGTH_MONEY',	// 6
		'PRICE_CHANGE',			// 7
		'ALREADY_EXISTS',		// 8
		'DONT_EXISTS',			// 9
	);

	$what = isset($_REQUEST['what']) ? $_REQUEST['what'] : '';
	$parameters = $_REQUEST;
	$identifier = isset($_REQUEST['identifier']) ? $_REQUEST['identifier'] : '';

	$results = '';
	$status = $availableStatus[4];

	if(!array_key_exists($what, $whats)) $status = $availableStatus[2]; // Check if all keys is set
	else if(strlen($identifier) != 40) $status = $availableStatus[1]; // Check identifier size
	else if(!validParameters($whats[$what], $parameters)) $status = $availableStatus[3]; // First parameter validation
	else if(!special_validation_work($whats[$what], $parameters, $availableStatus, $status)) ; // second validation
	else { // try to make the query
		try
		{
			$dbh = new PDO("mysql:host=$host;dbname=$database_name", $username, $password, array(
				PDO::ATTR_PERSISTENT => true // Set the connexion persitent for keep connexion in cache
				));
			$stmt = executePDOQueryForStoredProcedureNameAndData($dbh, $whats[$what]['name'], $parameters, $results);

			$out = array();

			while($data = $stmt->fetch(PDO::FETCH_ASSOC))
				$out[] = $data;

			if($whats[$what]['nullResult'])
				$results = "";
			else
			{
				if($whats[$what]['array'])
					$results = $out;
				else
					$results = $out[0];
			}

			$stmt->closeCursor();
			$result = $dbh->query("select @result")->fetch(PDO::FETCH_ASSOC);
			$status = $availableStatus[intval($result['@result'])];
		}
		catch(Exception $e)
		{
			$results = 'Error : '.$e->getMessage();
			$results .= ' with code : '.$e->getCode();
		}
	}

	header('Content-Type: application/json');
	echo json_encode(array('results' => $results, 'status' => $status));

	function validParameters($whats, &$parameters) {
		$formatedParameters = $whats['parameters'];

        foreach(array_keys($formatedParameters) as $key)
		{
			if(array_key_exists($key, $parameters))
				$formatedParameters[$key]['value'] = $parameters[$key];
			else
				return false;
		}
		$parameters = $formatedParameters;
		return true;
	}

	function executePDOQueryForStoredProcedureNameAndData(&$dbh, $storedProcedureName, $data, &$results)
	{
		$query = createPDOQueryForStoredProcedureNameAndKeys($storedProcedureName, array_keys($data));
		$stmt  = $dbh->prepare($query);
		appendDataToQuery($stmt, $data);
		try
        {
            $stmt->execute();
        }
        catch(PDOException $e)
        {
			$results = 'Error : '.$e->getMessage();
			$results .= ' with code : '.$e->getCode();
        }
        return $stmt;
	}

	function createPDOQueryForStoredProcedureNameAndKeys($storedProcedureName, $keys)
	{
		$query = 'CALL '.$storedProcedureName.'(';

		foreach($keys as $key)
			$query.=':'.$key.',';

		$query .= '@result)';

        return $query;
	}

	function appendDataToQuery(&$stmt, $data)
	{
        foreach($data as $key => $value)
            $stmt->bindParam(':'.$key, $value['value'], $value['type']);
	}

	function special_validation_work($what, &$parameters, &$availableStatus, &$status)
	{
		$facebook_check_token_me_path = 'https://graph.facebook.com/me?access_token=';
		$facebook_check_token_app_path = 'https://graph.facebook.com/app?access_token=';

		if(array_key_exists('lat', $parameters))
		{
			$lat = str_replace(',','.', $parameters['lat']['value']);
			$parameters['lat']['value'] = $lat;
			$lat = doubleval($lat);
			if($lat > 90 || $lat < -90)
			{
				$status = $availableStatus[3];
				return false;
			}
		}
		if(array_key_exists('lng', $parameters))
		{
			$lng = str_replace(',','.', $parameters['lng']['value']);
			$parameters['lng']['value'] = $lng;
			$lng = doubleval($lng);
			if($lng > 180 || $lng < -180)
			{
				$status = $availableStatus[3];
				return false;
			}
		}
		if(array_key_exists('limit', $parameters))
		{
			$limit = intval($parameters['limit']['value']);
			if($limit < 0)
			{
				$status = $availableStatus[3];
				return false;
			}
		}
		if(array_key_exists('start', $parameters))
		{
			$start = intval($parameters['start']['value']);
			if($start < 0)
			{
				$status = $availableStatus[3];
				return false;
			}
		}
		if(array_key_exists('price', $parameters))
		{
			$price = intval($parameters['price']['value']);
			if($price < 0)
			{
				$status = $availableStatus[3];
				return false;
			}
		}
		if(array_key_exists('token', $parameters)) // Need to be the last, because there is a "return true"
		{
			if(array_key_exists('user_id', $parameters))
			{
				$token = $parameters['token']['value'];
				$user_id = $parameters['user_id']['value'];
				$response = file_get_contents($facebook_check_token_app_path . $token);
				$json = json_decode($response);
				if($json->name == 'Corporations')
				{
					$response = file_get_contents($facebook_check_token_me_path . $token);
					$json = json_decode($response);
					if($json->id == $user_id)
					{
						unset($parameters['token']);
						return true;
					}
				}
			}
			$status = $availableStatus[3];
			return false;
		}
		return true;
	}
