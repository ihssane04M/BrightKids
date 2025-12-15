# Architecture du Projet BrightKids

## 📁 Structure Complète du Projet

```
brightkids/
│
├── 📄 .gitignore                          # Fichiers ignorés par Git
├── 📄 build.gradle.kts                    # Configuration Gradle racine
├── 📄 settings.gradle.kts                 # Paramètres du projet Gradle
├── 📄 gradle.properties                   # Propriétés Gradle
├── 📄 gradlew                             # Script Gradle wrapper (Unix)
├── 📄 gradlew.bat                         # Script Gradle wrapper (Windows)
│
├── 📁 .idea/                              # Configuration Android Studio
│   ├── .gitignore
│   ├── AndroidProjectSystem.xml
│   ├── compiler.xml
│   ├── deploymentTargetSelector.xml
│   ├── deviceManager.xml
│   ├── gradle.xml
│   ├── migrations.xml
│   ├── misc.xml
│   └── runConfigurations.xml
│
├── 📁 .kotlin/                            # Logs et cache Kotlin
│   └── errors/
│
├── 📁 gradle/                             # Configuration Gradle
│   ├── wrapper/
│   │   ├── gradle-wrapper.jar
│   │   └── gradle-wrapper.properties
│   └── libs.versions.toml                 # Versions des dépendances
│
├── 📁 app/                                # Module principal de l'application
│   ├── 📄 .gitignore
│   ├── 📄 build.gradle.kts                # Configuration du module app
│   ├── 📄 proguard-rules.pro              # Règles ProGuard
│   │
│   └── 📁 src/
│       │
│       ├── 📁 main/                       # Code source principal
│       │   ├── 📄 AndroidManifest.xml     # Manifeste Android
│       │   ├── 📄 ic_launcher-playstore.png
│       │   │
│       │   ├── 📁 java/com/example/brightkids/
│       │   │   │
│       │   │   ├── 🎯 ACTIVITÉS (Activities)
│       │   │   │   ├── SplashActivity.kt          # Écran de démarrage
│       │   │   │   ├── IntroActivity.kt           # Pages d'introduction
│       │   │   │   ├── OnboardingActivity.kt      # Configuration utilisateur
│       │   │   │   ├── MainActivity.kt            # Menu principal
│       │   │   │   ├── LetterListActivity.kt      # Liste des lettres
│       │   │   │   └── DrawingActivity.kt         # Jeu de tracé
│       │   │   │
│       │   │   ├── 📦 ADAPTERS
│       │   │   │   ├── LetterAdapter.kt           # Adapter pour les lettres
│       │   │   │   └── AvatarAdapter.kt           # Adapter pour les avatars
│       │   │   │
│       │   │   ├── 🗄️ DATABASE (Couche Données)
│       │   │   │   ├── AppDatabase.kt             # Base de données Room
│       │   │   │   └── LetterDao.kt               # Data Access Object
│       │   │   │
│       │   │   ├── 📊 MODEL (Modèles de Données)
│       │   │   │   ├── Letter.kt                  # Entité Lettre
│       │   │   │   └── LetterProgress.kt          # Entité Progression
│       │   │   │
│       │   │   ├── 🏛️ REPOSITORY (Couche Métier)
│       │   │   │   └── LetterRepository.kt        # Repository Pattern
│       │   │   │
│       │   │   ├── 🎨 VIEW (Vues Personnalisées)
│       │   │   │   └── DrawingView.kt             # Vue de dessin personnalisée
│       │   │   │
│       │   │   └── ⚙️ UTILS
│       │   │       └── PrefsManager.kt            # Gestionnaire de préférences
│       │   │
│       │   └── 📁 res/                     # Ressources Android
│       │       │
│       │       ├── 📁 drawable/            # Images et drawables
│       │       │   ├── 🎨 GRADIENTS
│       │       │   │   ├── gradient_background_home.xml
│       │       │   │   ├── gradient_background_drawing.xml
│       │       │   │   ├── gradient_background_letters.xml
│       │       │   │   ├── gradient_blue.xml
│       │       │   │   └── gradient_green.xml
│       │       │   │
│       │       │   ├── 🔘 BOUTONS
│       │       │   │   ├── gradient_button_blue_teal.xml
│       │       │   │   └── gradient_button_green.xml
│       │       │   │
│       │       │   ├── 🎯 ICÔNES VECTORIELLES
│       │       │   │   ├── ic_arrow_back.xml
│       │       │   │   ├── ic_back_arrow.xml
│       │       │   │   ├── ic_check.xml
│       │       │   │   ├── ic_clear.xml
│       │       │   │   ├── ic_home.xml
│       │       │   │   ├── ic_next_arrow.xml
│       │       │   │   ├── ic_play_sound.xml
│       │       │   │   ├── ic_restart.xml
│       │       │   │   └── ic_sparkles.xml
│       │       │   │
│       │       │   ├── 🔵 INDICATEURS
│       │       │   │   ├── indicator_dot.xml
│       │       │   │   ├── indicator_dot_active.xml
│       │       │   │   └── indicator_dot_inactive.xml
│       │       │   │
│       │       │   ├── 🖼️ BACKGROUNDS
│       │       │   │   ├── background_teal_dots.xml
│       │       │   │   ├── drawing_background.xml
│       │       │   │   ├── ic_launcher_background.xml
│       │       │   │   └── ic_launcher_foreground.xml
│       │       │   │
│       │       │   ├── 🎨 LOGO
│       │       │   │   └── logo_brightkids.png
│       │       │   │
│       │       │   └── 📝 AUTRES
│       │       │       └── ic_start_trace.xml
│       │       │
│       │       ├── 📁 font/                # Polices personnalisées
│       │       │   └── comic_sans.ttf
│       │       │
│       │       ├── 📁 layout/              # Layouts XML
│       │       │   ├── 🖥️ ACTIVITIES
│       │       │   │   ├── activity_splash.xml
│       │       │   │   ├── activity_intro.xml
│       │       │   │   ├── activity_onboarding.xml
│       │       │   │   ├── activity_main.xml
│       │       │   │   ├── activity_letter_list.xml
│       │       │   │   └── activity_drawing.xml
│       │       │   │
│       │       │   ├── 📋 ITEMS (RecyclerView)
│       │       │   │   ├── item_intro_page.xml
│       │       │   │   ├── item_onboarding_avatar.xml
│       │       │   │   ├── item_letter.xml
│       │       │   │   └── item_avatar.xml
│       │       │   │
│       │       │   └── 💬 DIALOGS
│       │       │       └── dialog_score.xml
│       │       │
│       │       ├── 📁 mipmap-*/            # Icônes de l'application (différentes densités)
│       │       │   ├── mipmap-mdpi/
│       │       │   ├── mipmap-hdpi/
│       │       │   ├── mipmap-xhdpi/
│       │       │   ├── mipmap-xxhdpi/
│       │       │   ├── mipmap-xxxhdpi/
│       │       │   └── mipmap-anydpi-v26/
│       │       │
│       │       ├── 📁 values/              # Valeurs de ressources
│       │       │   ├── colors.xml          # Palette de couleurs
│       │       │   ├── dimens.xml          # Dimensions
│       │       │   ├── strings.xml         # Chaînes de caractères
│       │       │   └── themes.xml          # Thèmes Material Design
│       │       │
│       │       ├── 📁 values-night/        # Thèmes pour le mode sombre
│       │       │   └── themes.xml
│       │       │
│       │       └── 📁 xml/                 # Configuration XML
│       │           ├── backup_rules.xml
│       │           └── data_extraction_rules.xml
│       │
│       ├── 📁 test/                        # Tests unitaires
│       │   └── java/com/example/brightkids/
│       │       └── ExampleUnitTest.kt
│       │
│       └── 📁 androidTest/                 # Tests d'instrumentation
│           └── java/com/example/brightkids/
│               └── ExampleInstrumentedTest.kt
│
├── 📁 Documentation/
│   ├── ARCHITECTURE.md                    # Architecture technique détaillée
│   ├── ARCHITECTURE_PROJET.md             # Ce document (structure physique)
│   └── COMPTE_RENDU.md                    # Compte-rendu des modifications
│
└── 📄 Fichiers divers
    ├── modeling.jpg
    └── téléchargement.png
```

---

## 📦 Organisation par Couches

### 🎯 **Couche Présentation (UI Layer)**

```
app/src/main/java/com/example/brightkids/
│
├── Activities/
│   ├── SplashActivity.kt          → Écran de démarrage
│   ├── IntroActivity.kt           → Introduction (3 pages)
│   ├── OnboardingActivity.kt      → Configuration utilisateur
│   ├── MainActivity.kt            → Menu principal
│   ├── LetterListActivity.kt      → Liste des lettres
│   └── DrawingActivity.kt         → Jeu de tracé
│
├── Adapters/
│   ├── LetterAdapter.kt           → RecyclerView pour lettres
│   └── AvatarAdapter.kt           → RecyclerView pour avatars
│
└── view/
    └── DrawingView.kt             → Vue personnalisée de dessin
```

**Layouts associés :**
- `activity_*.xml` → Layouts pour chaque activité
- `item_*.xml` → Layouts pour les items de RecyclerView
- `dialog_*.xml` → Layouts pour les dialogues

---

### 🏛️ **Couche Métier (Business Layer)**

```
app/src/main/java/com/example/brightkids/
│
└── repository/
    └── LetterRepository.kt        → Abstraction des opérations de données
```

**Responsabilités :**
- Encapsule la logique d'accès aux données
- Fournit une API simple pour les activités
- Utilise le DAO pour accéder à la base de données

---

### 🗄️ **Couche Données (Data Layer)**

```
app/src/main/java/com/example/brightkids/
│
├── database/
│   ├── AppDatabase.kt             → Configuration Room Database
│   └── LetterDao.kt               → Data Access Object
│
├── model/
│   ├── Letter.kt                  → Entité Lettre
│   └── LetterProgress.kt          → Entité Progression
│
└── PrefsManager.kt                → Gestionnaire SharedPreferences
```

**Responsabilités :**
- Persistance des données (Room)
- Gestion des préférences utilisateur (SharedPreferences)
- Définition des entités de données

---

## 🎨 Organisation des Ressources

### **Drawables** (`res/drawable/`)

```
drawable/
├── Gradients de fond
│   ├── gradient_background_home.xml
│   ├── gradient_background_drawing.xml
│   └── gradient_background_letters.xml
│
├── Boutons avec gradients
│   ├── gradient_button_blue_teal.xml
│   └── gradient_button_green.xml
│
├── Icônes vectorielles
│   ├── ic_arrow_back.xml
│   ├── ic_check.xml
│   ├── ic_clear.xml
│   ├── ic_next_arrow.xml
│   └── ...
│
├── Indicateurs de pages
│   ├── indicator_dot.xml
│   ├── indicator_dot_active.xml
│   └── indicator_dot_inactive.xml
│
└── Images bitmap
    └── logo_brightkids.png
```

### **Layouts** (`res/layout/`)

```
layout/
├── Activités
│   ├── activity_splash.xml
│   ├── activity_intro.xml
│   ├── activity_onboarding.xml
│   ├── activity_main.xml
│   ├── activity_letter_list.xml
│   └── activity_drawing.xml
│
├── Items RecyclerView
│   ├── item_intro_page.xml
│   ├── item_onboarding_avatar.xml
│   ├── item_letter.xml
│   └── item_avatar.xml
│
└── Dialogs
    └── dialog_score.xml
```

### **Values** (`res/values/`)

```
values/
├── colors.xml          → Palette de couleurs pastel
├── dimens.xml          → Dimensions standardisées
├── strings.xml         → Chaînes de caractères (i18n)
└── themes.xml          → Thèmes Material Design 3
```

---

## 🔧 Fichiers de Configuration

### **Gradle**

```
├── build.gradle.kts (racine)      → Configuration globale du projet
├── settings.gradle.kts            → Modules du projet
├── gradle.properties              → Propriétés Gradle
├── gradle/wrapper/                → Gradle Wrapper
│   ├── gradle-wrapper.jar
│   └── gradle-wrapper.properties
└── app/build.gradle.kts           → Configuration du module app
```

**Dépendances principales :**
- AndroidX Core, AppCompat, Material
- Room Database
- ViewPager2
- Coroutines
- Lifecycle (LiveData, ViewModel)

### **Android**

```
├── AndroidManifest.xml            → Déclaration des activités et permissions
├── proguard-rules.pro             → Règles de minification
└── res/xml/
    ├── backup_rules.xml           → Règles de sauvegarde
    └── data_extraction_rules.xml  → Règles d'extraction de données
```

---

## 📊 Flux de Données dans le Projet

```
┌─────────────────────────────────────────────────────────┐
│                    ACTIVITÉS (UI)                       │
│  Splash → Intro → Onboarding → Main → LetterList       │
│                                              ↓          │
│                                        DrawingActivity  │
└──────────────────────┬──────────────────────────────────┘
                       │
                       ↓
┌─────────────────────────────────────────────────────────┐
│                 REPOSITORY (Optionnel)                  │
│              LetterRepository                           │
└──────────────────────┬──────────────────────────────────┘
                       │
                       ↓
┌─────────────────────────────────────────────────────────┐
│                    DAO (Data Access)                    │
│                  LetterDao                              │
└──────────────────────┬──────────────────────────────────┘
                       │
                       ↓
┌─────────────────────────────────────────────────────────┐
│              ROOM DATABASE                              │
│  ┌──────────────┐      ┌──────────────────┐           │
│  │  letters     │      │  progress        │           │
│  │  table       │      │  table           │           │
│  └──────────────┘      └──────────────────┘           │
└─────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────┐
│              SHARED PREFERENCES                         │
│  PrefsManager → intro_completed                        │
│              → onboarding_completed                     │
│              → user_name                                │
│              → user_avatar                              │
└─────────────────────────────────────────────────────────┘
```

---

## 🔄 Cycle de Vie des Activités

```
1. SplashActivity (Launcher)
   │
   ├─→ Vérifie PrefsManager
   │
   ├─→ Si intro non complétée
   │   └─→ IntroActivity
   │       └─→ OnboardingActivity
   │           └─→ MainActivity
   │
   ├─→ Si onboarding non complété
   │   └─→ OnboardingActivity
   │       └─→ MainActivity
   │
   └─→ Sinon
       └─→ MainActivity
           │
           ├─→ LetterListActivity (Arabe)
           │   └─→ DrawingActivity
           │       ├─→ DrawingActivity (suivant)
           │       └─→ LetterListActivity (retour)
           │
           └─→ LetterListActivity (Français)
               └─→ DrawingActivity (même flux)
```

---

## 📝 Conventions de Nommage

### **Fichiers Kotlin**
- **Activities** : `*Activity.kt` (ex: `SplashActivity.kt`)
- **Adapters** : `*Adapter.kt` (ex: `LetterAdapter.kt`)
- **Models** : Nom simple (ex: `Letter.kt`, `LetterProgress.kt`)
- **Views** : `*View.kt` (ex: `DrawingView.kt`)
- **Managers** : `*Manager.kt` (ex: `PrefsManager.kt`)
- **DAO** : `*Dao.kt` (ex: `LetterDao.kt`)
- **Database** : `*Database.kt` (ex: `AppDatabase.kt`)

### **Fichiers XML**
- **Layouts Activities** : `activity_*.xml` (ex: `activity_main.xml`)
- **Layouts Items** : `item_*.xml` (ex: `item_letter.xml`)
- **Layouts Dialogs** : `dialog_*.xml` (ex: `dialog_score.xml`)
- **Drawables** : `ic_*.xml` pour icônes, `gradient_*.xml` pour gradients
- **Colors** : `colors.xml`
- **Strings** : `strings.xml`
- **Themes** : `themes.xml`

### **IDs de Ressources**
- **Views** : `tv*` pour TextView, `btn*` pour Button, `iv*` pour ImageView
- **Layouts** : `@+id/activityName`, `@+id/itemName`
- **Colors** : `@color/color_name` (ex: `@color/teal_600`)
- **Drawables** : `@drawable/resource_name` (ex: `@drawable/ic_check`)

---

## 🎯 Points d'Entrée Principaux

1. **Point d'entrée** : `SplashActivity` (déclaré dans AndroidManifest.xml comme LAUNCHER)
2. **Base de données** : `AppDatabase.getDatabase(context)`
3. **Préférences** : `PrefsManager.getInstance(context)`
4. **Navigation principale** : `MainActivity`

---

## 📈 Évolutivité

### **Ajout de nouvelles fonctionnalités**

1. **Nouvelle activité** :
   - Créer `NewActivity.kt` dans `java/com/example/brightkids/`
   - Créer `activity_new.xml` dans `res/layout/`
   - Déclarer dans `AndroidManifest.xml`

2. **Nouvelle entité** :
   - Créer `NewEntity.kt` dans `model/`
   - Ajouter dans `AppDatabase.kt` (entities array)
   - Créer `NewEntityDao.kt` dans `database/`

3. **Nouvelle ressource** :
   - Ajouter dans le dossier approprié (`drawable/`, `layout/`, etc.)
   - Référencer dans le code avec `R.resource_type.resource_name`

---

## 🔍 Fichiers Clés à Consulter

| Fichier | Description |
|---------|-------------|
| `AndroidManifest.xml` | Configuration de l'application |
| `AppDatabase.kt` | Structure de la base de données |
| `PrefsManager.kt` | Gestion de l'état de l'application |
| `SplashActivity.kt` | Logique de navigation initiale |
| `DrawingView.kt` | Logique de dessin et scoring |
| `colors.xml` | Palette de couleurs |
| `build.gradle.kts` | Dépendances du projet |

---

*Document généré le : $(date)*
*Version du projet : 1.0*
