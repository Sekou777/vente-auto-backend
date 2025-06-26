#!/bin/bash
cd "$(dirname "$0")"

echo "🚦 Arrêt du serveur Spring Boot..."
# Trouver le processus Java en cours lié au projet et le tuer
PID=$(ps -ef | grep 'vente_auto' | grep -v grep | awk '{print $2}')
if [ -n "$PID" ]; then
  kill -9 $PID
  echo "✅ Serveur arrêté (PID: $PID)"
else
  echo "⚠️ Aucun serveur à arrêter"
fi

echo "🛠️ Compilation du projet..."
./mvnw clean package -DskipTests

echo "🚀 Démarrage du serveur Spring Boot..."
nohup java -jar target/vente_auto-0.0.1-SNAPSHOT.jar > logs.txt 2>&1 &

echo "✅ Serveur lancé ! Tu peux vérifier avec : tail -f logs.txt"
echo "📂 Vérification des logs..."
tail -f logs.txt | grep -E "Started|Error|Exception"
echo "📦 Vérification de la présence du fichier JAR..."
if [ -f target/vente_auto-0.0.1-SNAPSHOT.jar ]; then
  echo "✅ Fichier JAR trouvé !"
else
  echo "⚠️ Fichier JAR manquant !"
fi