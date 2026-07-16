# EvolutionHoe 🌾

Un plugin Spigot/Paper premium pour les serveurs farming.

## 🎯 Fonctionnalités Principales

### 🌾 Progression
- ✅ Système XP avancé
- ✅ Niveaux illimités
- ✅ Prestige illimité
- ✅ Statistiques détaillées

### 🌱 Farming
- ✅ Fortune progressive
- ✅ Auto-replant
- ✅ Récolte 3x3, 5x5, circulaire
- ✅ Double/Triple récolte

### 💰 Économie
- ✅ Sell All & Auto Sell
- ✅ Multiplicateurs de vente
- ✅ Bonus dynamiques

### 🔑 Loot Système
- ✅ Clés, Tokens, Cristaux
- ✅ Essence & Fragments
- ✅ Lootbox animées

### 🍀 Chance
- ✅ Lucky Harvest
- ✅ Jackpot
- ✅ Critical Harvest
- ✅ Double XP

### ⚡ Utilitaires
- ✅ Auto Repair
- ✅ Durabilité infinie
- ✅ Aimant magnétique
- ✅ Haste automatique

### 🎨 Cosmétiques
- ✅ Skins Nexo intégrés
- ✅ Particules personnalisées
- ✅ Sons ambiance
- ✅ Animations fluides

### 🧬 Système Génétique (Unique)
- ✅ ADN unique par houe
- ✅ Mutations aléatoires
- ✅ Traits personnalisés
- ✅ Fusion de houes

### 💎 Gemmes & Upgrades
- ✅ Rubis, Saphir, Émeraude, Obsidienne
- ✅ Système de titres
- ✅ Éveil au niveau 100

### 📊 Interface
- ✅ GUI animée
- ✅ Barre XP intégrée
- ✅ BossBar & ActionBar
- ✅ Leaderboard

### 🗄️ Stockage
- ✅ SQLite & MySQL
- ✅ PlaceholderAPI
- ✅ Reload sans redémarrage

## 📦 Structure

```
EvolutionHoe/
├── src/
│   ├── api/              # APIs et interfaces
│   ├── commands/         # Commandes principales
│   ├── config/           # Gestion des configurations
│   ├── data/             # Modèles de données
│   ├── database/         # Accès base de données
│   ├── enchantments/     # Enchantements custom
│   ├── events/           # Listeners d'événements
│   ├── gui/              # Interfaces utilisateur
│   ├── hooks/            # Intégrations (Nexo, etc)
│   ├── items/            # Gestion des items
│   ├── listeners/        # Event handlers
│   ├── managers/         # Gestionnaires de système
│   ├── models/           # Models métier
│   ├── storage/          # Persistance de données
│   ├── tasks/            # Tâches asynchrones
│   ├── upgrades/         # Système d'upgrades
│   └── utils/            # Utilitaires
│
└── resources/
    ├── plugin.yml        # Configuration Spigot
    ├── config.yml        # Config générale
    ├── upgrades.yml      # Définition des upgrades
    ├── gui.yml           # Layout des GUIs
    ├── messages.yml      # Messages internationalisés
    ├── levels.yml        # Tables de niveaux
    ├── prestige.yml      # Config prestige
    ├── drops.yml         # Table de loot
    ├── particles.yml     # Effets visuels
    ├── sounds.yml        # Effets sonores
    └── skins.yml         # Skins Nexo
```

## 🚀 Installation

1. Cloner le repo
2. `mvn clean package`
3. Placer le JAR dans `plugins/`
4. Redémarrer le serveur
5. Configurer les fichiers YAML

## 📝 License

Tous droits réservés © 2026
