SELECT R.ROLE_ID, R.RESOURCE_ID, R.RESOURCE_TYPE
FROM ROLE_RESOURCE R
WHERE R.RESOURCE_ID = :resourceId