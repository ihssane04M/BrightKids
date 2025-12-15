# Architecture de l'Application BrightKids

## 📱 Vue d'ensemble

**BrightKids** est une application Android éducative développée en Kotlin pour apprendre aux enfants à tracer les lettres de l'alphabet français et arabe. L'application suit une architecture modulaire avec des composants séparés pour la présentation, les données et la logique métier.

---

## 🏗️ Architecture Générale

L'application suit une **architecture en couches** avec les composants suivants :

```
┌─────────────────────────────────────────────────┐
│           COUCHE PRÉSENTATION (UI)              │
│  Activities, Adapters, Custom Views, Layouts    │
├─────────────────────────────────────────────────┤
│           COUCHE MÉTIER (BUSINESS)              │
│         Repository Pattern (optionnel)          │
├─────────────────────────────────────────────────┤
│      COUCHE DONNÉES (DATA)                      │
│     Room Database, SharedPreferences            │
└─────────────────────────────────────────────────┘
```

---

## 📦 Structure des Packages

```
com.example.brightkids/
├── database/          # Couche d'accès aux données
│   ├── AppDatabase.kt      # Base de données Room
│   └── LetterDao.kt        # Data Access Object pour les lettres
│
├── model/             # Modèles de données
│   ├── Letter.kt           # Entité Lettre
│   └── LetterProgress.kt   # Entité Progression
│
├── repository/        # Pattern Repository (couche métier)
│   └── LetterRepository.kt # Repository pour les lettres
│
├── view/              # Vues personnalisées
│   └── DrawingView.kt      # Vue de dessin personnalisée
│
├── SplashActivity.kt       # Activité de démarrage
├── IntroActivity.kt        # Activité d'introduction
├── OnboardingActivity.kt   # Activité d'onboarding
├── MainActivity.kt         # Activité principale (menu)
├── LetterListActivity.kt   # Liste des lettres
├── DrawingActivity.kt      # Activité de tracé
├── PrefsManager.kt         # Gestionnaire de préférences
├── AvatarAdapter.kt        # Adapter pour les avatars
└── LetterAdapter.kt        # Adapter pour les lettres
```

---

## 🔄 Flux de Navigation

```
SplashActivity (Launcher)
    │
    ├─→ IntroActivity (si intro non complétée)
    │       │
    │       └─→ OnboardingActivity (si onboarding non complété)
    │               │
    │               └─→ MainActivity
    │
    ├─→ OnboardingActivity (si intro complétée mais onboarding non complété)
    │       │
    │       └─→ MainActivity
    │
    └─→ MainActivity (si tout est complété)
            │
            ├─→ LetterListActivity (Arabe)
            │       │
            │       └─→ DrawingActivity
            │               │
            │               ├─→ DrawingActivity (lettre suivante)
            │               │
            │               └─→ LetterListActivity (retour avec mise à jour)
            │
            └─→ LetterListActivity (Français)
                    │
                    └─→ DrawingActivity (même flux)
```

---

## 🎯 Composants Principaux

### 1. **Activités (Activities)**

#### **SplashActivity**
- **Rôle** : Activité de lancement, point d'entrée de l'application
- **Responsabilités** :
  - Afficher le logo et le titre avec animations
  - Décider de la navigation initiale selon l'état de l'application (`PrefsManager`)
  - Naviguer vers `IntroActivity`, `OnboardingActivity` ou `MainActivity`

#### **IntroActivity**
- **Rôle** : Présenter l'application (3 pages d'introduction)
- **Fonctionnalités** :
  - `ViewPager2` pour le défilement des pages
  - Indicateurs de pages (dots)
  - Boutons "Passer" et "Suivant"
  - Enregistre l'état `intro_completed` dans `PrefsManager`

#### **OnboardingActivity**
- **Rôle** : Collecter les informations de l'utilisateur (nom, avatar)
- **Fonctionnalités** :
  - `ViewPager2` avec une seule page pour le profil
  - Sélection d'avatar via `RecyclerView`
  - Saisie du nom via `TextInputLayout`
  - Validation et sauvegarde des données utilisateur
  - Navigation vers `MainActivity` après complétion

#### **MainActivity**
- **Rôle** : Menu principal, choix de la langue (Arabe/Français)
- **Fonctionnalités** :
  - Deux boutons pour sélectionner la langue
  - Navigation vers `LetterListActivity` avec la langue sélectionnée

#### **LetterListActivity**
- **Rôle** : Afficher la liste des lettres et la progression
- **Fonctionnalités** :
  - Affichage des lettres via `RecyclerView` avec `LetterAdapter`
  - Affichage du pourcentage de progression globale
  - Navigation vers `DrawingActivity` pour chaque lettre
  - Mise à jour automatique de la progression via `onActivityResult` et `onResume`
  - Utilise des coroutines pour récupérer les données de la base

#### **DrawingActivity**
- **Rôle** : Jeu de tracé de lettres
- **Fonctionnalités** :
  - Affichage de la lettre à tracer via `DrawingView`
  - TextToSpeech pour prononcer la lettre
  - Calcul du score basé sur le tracé
  - Affichage du dialog de score avec étoiles animées
  - Sauvegarde de la progression dans la base de données
  - Navigation automatique vers la lettre suivante si 3 étoiles obtenues
  - Gestion du retour avec `setResult(RESULT_OK)`

---

### 2. **Vues Personnalisées (Custom Views)**

#### **DrawingView**
- **Rôle** : Zone de dessin pour tracer les lettres
- **Fonctionnalités** :
  - Détection des gestes tactiles (`onTouchEvent`)
  - Affichage de la lettre guide (lettre en gris clair)
  - Dessin du tracé utilisateur (couleur rose/rouge)
  - Calcul du score basé sur :
    - Nombre de points de touche
    - Durée du tracé
  - Conversion du score en étoiles (0-3)
  - Callback `onDrawingComplete` pour notifier l'activité parente
  - Méthode `clear()` pour réinitialiser le dessin

---

### 3. **Base de Données (Room Database)**

#### **AppDatabase**
- **Type** : Base de données Room (singleton)
- **Entités** :
  - `Letter` : Informations sur les lettres
  - `LetterProgress` : Progression de chaque lettre
- **Version** : 1
- **Nom de la base** : `kids_learning_database`

#### **LetterDao** (Data Access Object)
- **Méthodes principales** :
  - `getLettersByLanguage(language: String)`: LiveData<List<Letter>>
  - `insertLetter(letter: Letter)`: suspend
  - `getProgress(letterId: Int)`: LiveData<LetterProgress?>
  - `insertProgress(progress: LetterProgress)`: suspend
  - `incrementPractice(letterId: Int)`: suspend
  - `updateStarsIfBetter(letterId: Int, stars: Int)`: suspend
  - `updateStars(letterId: Int, stars: Int)`: suspend
  - `insertProgressIfNotExists(letterId: Int, stars: Int)`: suspend
  - `getAllProgress()`: suspend List<LetterProgress>
  - `getCompletedLettersCount()`: suspend Int
  - `getProgressForLetters(letterIds: List<Int>)`: suspend List<LetterProgress>

#### **Letter** (Entity)
```kotlin
@Entity(tableName = "letters")
data class Letter(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val letter: String,        // La lettre elle-même (ex: "A", "ا")
    val name: String,          // Nom de la lettre (ex: "Alif", "A")
    val language: String,      // "arabic" ou "french"
    val soundUrl: String = ""  // URL du son (optionnel)
)
```

#### **LetterProgress** (Entity)
```kotlin
@Entity(tableName = "progress")
data class LetterProgress(
    @PrimaryKey val letterId: Int,
    val practiceCount: Int = 0,  // Nombre de fois pratiqué
    val stars: Int = 0            // Nombre d'étoiles (0-3)
)
```

---

### 4. **Gestion des Préférences**

#### **PrefsManager**
- **Type** : Singleton utilisant `SharedPreferences`
- **Responsabilités** :
  - Stocker l'état de complétion de l'intro
  - Stocker l'état de complétion de l'onboarding
  - Stocker le nom de l'utilisateur
  - Stocker l'avatar sélectionné
- **Clés utilisées** :
  - `intro_completed`
  - `onboarding_completed`
  - `user_name`
  - `user_avatar`

---

### 5. **Adapters (RecyclerView)**

#### **LetterAdapter**
- **Rôle** : Adapter pour afficher les lettres dans `LetterListActivity`
- **Fonctionnalités** :
  - Affiche la lettre, son nom, et les étoiles obtenues
  - Affiche un indicateur de progression
  - Gère les clics pour naviguer vers `DrawingActivity`

#### **AvatarAdapter**
- **Rôle** : Adapter pour afficher les avatars dans `OnboardingActivity`
- **Fonctionnalités** :
  - Affiche une grille d'avatars sélectionnables
  - Gère la sélection visuelle (bordure/indicateur)

---

## 🎨 Ressources (Resources)

### **Layouts**
- `activity_splash.xml` : Écran de démarrage
- `activity_intro.xml` : Pages d'introduction (ViewPager2)
- `item_intro_page.xml` : Layout d'une page d'intro
- `activity_onboarding.xml` : Onboarding avec ViewPager2
- `item_onboarding_avatar.xml` : Layout de la page de sélection d'avatar
- `activity_main.xml` : Menu principal
- `activity_letter_list.xml` : Liste des lettres
- `item_letter.xml` : Item d'une lettre dans la liste
- `activity_drawing.xml` : Activité de tracé
- `dialog_score.xml` : Dialog affichant le score et les étoiles

### **Drawables**
- Gradients de fond (`gradient_background_*.xml`)
- Boutons avec gradients (`gradient_button_*.xml`)
- Icônes vectorielles (`ic_*.xml`)
- Indicateurs de pages (`indicator_dot*.xml`)
- Logo (`logo_brightkids.png`)

### **Colors**
- Palette pastel (bleu, turquoise, vert, rose, jaune)
- Couleurs personnalisées pour l'onboarding, les boutons, etc.

---

## 🔧 Technologies et Bibliothèques

### **Android Jetpack**
- **Room** : Persistance des données
- **ViewBinding** : Liaison des vues (remplace `findViewById`)
- **Lifecycle** : Gestion du cycle de vie (LiveData, ViewModel)
- **Coroutines** : Programmation asynchrone

### **Material Design**
- **Material Components** : Boutons, TextInputLayout, CardView, etc.
- **Material Design 3** : Thèmes et styles modernes

### **Autres Bibliothèques**
- **ViewPager2** : Pagination pour intro et onboarding
- **Gson** : Sérialisation JSON (si nécessaire pour les données)

---

## 🔐 Gestion de l'État

### **État de l'Application**
- Stocké dans `SharedPreferences` via `PrefsManager`
- Détermine le flux de navigation au démarrage

### **État des Données**
- Stocké dans la base de données Room
- Progression des lettres synchronisée en temps réel via `LiveData`
- Mise à jour automatique lors du retour à `LetterListActivity`

### **État de l'UI**
- Géré par les activités elles-mêmes
- Animations et transitions entre activités

---

## 📊 Flux de Données

### **Lecture des Données**
```
Activity → Repository (optionnel) → DAO → Room Database → LiveData → Activity (UI)
```

### **Écriture des Données**
```
Activity → Coroutine (IO Dispatcher) → DAO → Room Database
```

### **Progression des Lettres**
```
DrawingActivity 
    → Calcul du score 
    → Conversion en étoiles 
    → saveProgress() (coroutine)
    → LetterDao.insertProgressIfNotExists() / updateStarsIfBetter()
    → Room Database
    → LetterListActivity.updateProgress() (coroutine)
    → UI mise à jour
```

---

## 🎮 Logique Métier Principale

### **Calcul du Score**
- Basé sur le nombre de points de touche
- Pénalité si trop rapide (< 1 seconde)
- Bonus si durée optimale (2-8 secondes)
- Pénalité si trop lent (> 15 secondes)
- Score converti en étoiles :
  - 0 étoile : score < 20
  - 1 étoile : score >= 20
  - 2 étoiles : score >= 40
  - 3 étoiles : score >= 70

### **Navigation Automatique**
- Si 3 étoiles obtenues → Navigation automatique vers la lettre suivante (après 2.5s)
- Possibilité de "Réessayer" (recharge la même lettre)
- Possibilité de "Continuer" (passe à la lettre suivante)

### **Progression Globale**
- Calculée en pourcentage : `(lettres complétées / total des lettres) * 100`
- Une lettre est "complétée" si elle a au moins 1 étoile (`stars >= 1`)
- Mise à jour automatique dans `LetterListActivity`

---

## 🚀 Points d'Amélioration Potentiels

1. **Architecture** :
   - Implémenter `ViewModel` pour séparer la logique UI de la logique métier
   - Utiliser `Navigation Component` pour une navigation plus structurée
   - Créer des `UseCase` pour encapsuler la logique métier

2. **Performance** :
   - Optimiser les requêtes de base de données
   - Ajouter de la pagination si le nombre de lettres augmente
   - Mettre en cache les images et ressources

3. **Fonctionnalités** :
   - Ajouter des sons pour chaque lettre
   - Implémenter un système de récompenses/badges
   - Ajouter des statistiques détaillées
   - Mode multijoueur ou défis

4. **Code Quality** :
   - Ajouter des tests unitaires et d'intégration
   - Utiliser `Dependency Injection` (Hilt/Koin)
   - Documenter davantage le code avec KDoc

---

## 📝 Notes Techniques

- **Min SDK** : 24 (Android 7.0)
- **Target SDK** : 34 (Android 14)
- **Compile SDK** : 34
- **Kotlin Version** : Compatible avec Kotlin 1.7+
- **Java Version** : 17
- **Orientation** : Portrait uniquement (toutes les activités)

---

## 🔍 Fichiers Clés à Examiner

Pour comprendre l'architecture en détail, examiner ces fichiers dans cet ordre :

1. `AndroidManifest.xml` : Configuration des activités
2. `AppDatabase.kt` : Structure de la base de données
3. `PrefsManager.kt` : Gestion de l'état de l'application
4. `SplashActivity.kt` : Point d'entrée et navigation initiale
5. `DrawingView.kt` : Logique de dessin et scoring
6. `DrawingActivity.kt` : Logique métier principale
7. `LetterListActivity.kt` : Affichage et gestion de la progression

---

*Document généré le : $(date)*
*Version de l'application : 1.0*
