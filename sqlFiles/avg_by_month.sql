SELECT
_type as type,
_unit as unit,
CASE WHEN _type IN (
'HKQuantityTypeIdentifierDistanceWalkingRunning',
'HKQuantityTypeIdentifierAppleStandTime',
'HKQuantityTypeIdentifierStepCount')
THEN AVG(_value) * 100
WHEN _type ='HKQuantityTypeIdentifierActiveEnergyBurned'
THEN AVG(_value) * 1000
ELSE AVG(_value)
END AS `value`,
date_format(_endDate, 'y-M') as month
FROM health_kit_data
WHERE _type like 'HKQuantityTypeIdentifier%'
GROUP BY date_format(_endDate, 'y-M'), _type, _unit
ORDER BY date_format(_endDate, 'y-M') ASC