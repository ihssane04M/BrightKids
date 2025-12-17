# 📍 Localisation des Options de Son dans BrightKids

## 🎵 Où se trouvent les options de son ?

### **1. Dans l'écran de tracé (DrawingActivity)**

#### **📍 Bouton Play Sound**
- **Fichier Layout** : `app/src/main/res/layout/activity_drawing.xml`
- **Lignes** : 143-152
- **ID du bouton** : `@+id/btnPlaySound`
- **Type** : `FloatingActionButton` (bouton flottant vert)
- **Icône** : `@drawable/ic_play_sound`
- **Position** : En bas de l'écran, premier bouton à gauche

```xml
<com.google.android.material.floatingactionbutton.FloatingActionButton
    android:id="@+id/btnPlaySound"
    android:src="@drawable/ic_play_sound"
    app:backgroundTint="@color/green_500"
    android:contentDescription="@string/play_sound"/>
```

#### **📍 Code Kotlin - Initialisation**
- **Fichier** : `app/src/main/java/com/example/brightkids/DrawingActivity.kt`
- **Ligne 25** : Déclaration du TTS
  ```kotlin
  private lateinit var tts: TextToSpeech
  ```
- **Ligne 42** : Initialisation dans `onCreate()`
  ```kotlin
  initializeTTS()
  ```
- **Lignes 62-70** : Fonction `initializeTTS()`
  ```kotlin
  private fun initializeTTS() {
      tts = TextToSpeech(this) { status ->
          if (status == TextToSpeech.SUCCESS) {
              val locale = if (language == "arabic") Locale("ar") else Locale.FRENCH
              tts.language = locale
              speakLetter()
          }
      }
  }
  ```

#### **📍 Code Kotlin - Action du bouton**
- **Ligne 54** : Clic sur le bouton
  ```kotlin
  binding.btnPlaySound.setOnClickListener { speakLetter() }
  ```
- **Lignes 304-306** : Fonction qui joue le son
  ```kotlin
  private fun speakLetter() {
      tts.speak(letterName, TextToSpeech.QUEUE_FLUSH, null, null)
  }
  ```

#### **📍 Nettoyage**
- **Lignes 308-311** : Arrêt du TTS dans `onDestroy()`
  ```kotlin
  override fun onDestroy() {
      tts.shutdown()
      super.onDestroy()
  }
  ```

---

### **2. Dans la liste des lettres (LetterListActivity)**

#### **📍 Code Kotlin - Initialisation**
- **Fichier** : `app/src/main/java/com/example/brightkids/LetterListActivity.kt`
- **Ligne 23** : Déclaration du TTS
  ```kotlin
  private lateinit var tts: TextToSpeech
  ```
- **Ligne 36** : Initialisation dans `onCreate()`
  ```kotlin
  initializeTTS()
  ```
- **Lignes 104-110** : Fonction `initializeTTS()`
  ```kotlin
  private fun initializeTTS() {
      tts = TextToSpeech(this) { status ->
          if (status == TextToSpeech.SUCCESS) {
              tts.language = if (language == "arabic") Locale("ar") else Locale.FRENCH
          }
      }
  }
  ```

#### **📍 Code Kotlin - Action automatique au clic**
- **Ligne 49-52** : Dans `setupRecyclerView()`, quand on clique sur une lettre
  ```kotlin
  val adapter = LetterAdapter(letters) { letter ->
      speakLetter(letter)  // ← Son joué automatiquement
      openDrawingActivity(letter)
  }
  ```
- **Lignes 112-114** : Fonction qui joue le son
  ```kotlin
  private fun speakLetter(letter: Letter) {
      tts.speak(letter.name, TextToSpeech.QUEUE_FLUSH, null, null)
  }
  ```

#### **📍 Nettoyage**
- **Lignes 197-200** : Arrêt du TTS dans `onDestroy()`
  ```kotlin
  override fun onDestroy() {
      tts.shutdown()
      super.onDestroy()
  }
  ```

---

## 🎨 Ressources visuelles

### **Icône du bouton Play Sound**
- **Fichier** : `app/src/main/res/drawable/ic_play_sound.xml`
- **Type** : Icône vectorielle (haut-parleur)

---

## 🔧 Technologies utilisées

### **TextToSpeech (TTS)**
- **Import** : `android.speech.tts.TextToSpeech`
- **Langues supportées** :
  - Arabe : `Locale("ar")`
  - Français : `Locale.FRENCH`
- **Fonctionnement** : Synthèse vocale native Android (pas de fichiers audio)

---

## 📋 Récapitulatif des emplacements

| Élément | Fichier | Lignes | Description |
|---------|---------|--------|-------------|
| **Bouton Play Sound** | `activity_drawing.xml` | 143-152 | Bouton vert flottant |
| **Initialisation TTS** | `DrawingActivity.kt` | 62-70 | `initializeTTS()` |
| **Action bouton** | `DrawingActivity.kt` | 54 | `btnPlaySound.setOnClickListener` |
| **Fonction play** | `DrawingActivity.kt` | 304-306 | `speakLetter()` |
| **Initialisation TTS** | `LetterListActivity.kt` | 104-110 | `initializeTTS()` |
| **Son au clic lettre** | `LetterListActivity.kt` | 49-52 | Dans l'adapter RecyclerView |
| **Fonction play** | `LetterListActivity.kt` | 112-114 | `speakLetter(letter)` |
| **Icône** | `ic_play_sound.xml` | - | Drawable vectoriel |

---

## 🎯 Comment modifier le comportement du son ?

### **Changer la langue**
Modifiez dans `initializeTTS()` :
```kotlin
tts.language = if (language == "arabic") Locale("ar") else Locale.FRENCH
```

### **Changer le texte prononcé**
Dans `DrawingActivity.kt`, ligne 305 :
```kotlin
tts.speak(letterName, ...)  // Changez letterName pour autre chose
```

### **Ajouter un paramètre de vitesse**
```kotlin
tts.setSpeechRate(0.8f)  // 0.5 = lent, 1.0 = normal, 2.0 = rapide
```

### **Ajouter un paramètre de hauteur**
```kotlin
tts.setPitch(1.2f)  // 0.5 = grave, 1.0 = normal, 2.0 = aigu
```

---

*Document créé pour localiser toutes les options de son dans le projet*



