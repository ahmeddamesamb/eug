# ETAPE 0
- Sur le dossier exemple copier le fichier docker-compose.yml
- Coller le fichier docker-compose.yml sur le dossier keycloak à la racine du dossier eugb
- A l'interieur du fichier docker-compose.yml , sur (volumes: ) mettre le chemin vers le dossier keycloak
- Se placer au répertoire eugb/keycloak et lancer la commande docker-compose up --build

# ETAPE 1
- Sur le dossier exemple copier le fichier environment-home.ts
- Coller sur home/src/app/config le fichier environment-home.ts 
- Renommer le fichier environment-home.ts en environment.ts
- Remplacer urlLogin par l'url de votre serveur keycloak
- Lancer la commande ng serve --port 4200

# ETAPE 2
- Sur le dossier exemple copier le fichier environment-pats.ts
- Copier le fichier environment-pats.ts sur pats/src/app/config
- Renommer le fichier environment-pats.ts en environment.ts
- Sur le dossier exemple copier le fichier keycloak.environment-pats.ts
- Renommer le fichier keycloak.environment-pats.ts en keycloak.environment.ts
- sur le fichier keycloak.environment.ts remplacer url par l'url de votre serveur keycloak
- Lancer la commance ng serve --port 4201



