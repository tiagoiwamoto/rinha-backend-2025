#!/bin/bash

# URLs dos serviços
DEFAULT_URL="http://localhost:8001/payments/service-health"
FALLBACK_URL="http://localhost:8002/payments/service-health"

# Realiza as requisições
responseDefault=$(curl -s -X GET "$DEFAULT_URL" -H "Content-Type: application/json")
responseFallback=$(curl -s -X GET "$FALLBACK_URL" -H "Content-Type: application/json")

# Extrai os valores de `failing` e `minResponseTime` (assumindo que estão no formato JSON)
defaultFailing=$(echo "$responseDefault" | jq -r '.failing')
defaultMinResponseTime=$(echo "$responseDefault" | jq -r '.minResponseTime')
fallbackMinResponseTime=$(echo "$responseFallback" | jq -r '.minResponseTime')

# Validação
if [[ "$defaultFailing" == "true" || "$defaultMinResponseTime" -gt "$fallbackMinResponseTime" ]]; then
    export SERVICE_STATUS="FALLBACK"
#    echo "export SERVICE_STATUS=$SERVICE_STATUS" >> ~/.bashrc
#    source ~/.bashrc
else
    export SERVICE_STATUS="DEFAULT"
#    echo "export SERVICE_STATUS=$SERVICE_STATUS" >> ~/.bashrc
#    source ~/.bashrc
fi

# Exibe o resultado
echo "$SERVICE_STATUS"