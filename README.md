Application will start at:  http://localhost:8081

API Endpoint: POST (/applications)

Request CURL:
curl --request POST \
  --url http://localhost:8081/applications \
  --header 'content-type: application/json' \
  --data '{
  "applicant": {
    "name": "Pradip",
    "age": 27,
    "monthlyIncome": 75000,
    "employmentType": "SALARIED",
    "creditScore": 609
  },
  "loan": {
    "amount": 500000,
    "tenureMonths": 24,
    "purpose": "PERSONAL"
  }
}'

Success responses example:
{
  "applicationId": "9fa3ff51-39c8-4861-9e68-909782d4d229",
  "offer": {
    "emi": 24243.32,
    "interestRate": 15,
    "tenureMonths": 24,
    "totalPayable": 581839.68
  },
  "riskBand": "HIGH",
  "status": "APPROVED"
}

