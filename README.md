# Caccia al Tesoro SAM

App Android per organizzare e giocare a una caccia al tesoro di gruppo, realizzata come progetto
d'esame per il corso di Sviluppo Applicazioni Mobili (V. Gervasi, a.a. 2025/26).

## Idea del gioco

Un giocatore assume il ruolo di **master**: prepara una caccia visitando fisicamente dei luoghi in
un certo ordine, registrando per ciascuno un indizio vocale la cui soluzione è il luogo successivo.
L'ultimo luogo è il tesoro.

Al momento del gioco, il master condivide la caccia localmente (Bluetooth) con i **player**
fisicamente presenti. Ogni player riceve il primo indizio e si muove per conto proprio: l'app non dà
indicazioni finché non ci si avvicina al luogo giusto, poi fornisce un segnale generico di
prossimità, e infine conferma l'arrivo sbloccando l'indizio successivo. Il gioco termina quando
tutti i player trovano il tesoro o abbandonano.

## Framework Android utilizzati

- **Jetpack Compose** — interfaccia utente
- **Room** — persistenza locale di cacce, luoghi e indizi
- **MediaRecorder / MediaPlayer** — registrazione e riproduzione degli indizi vocali
- **FusedLocationProviderClient** — geolocalizzazione dei luoghi e rilevamento di prossimità
- **Bluetooth (BluetoothAdapter, discovery, socket RFCOMM)** — condivisione locale della caccia tra
  master e player
- **Navigation Compose** — navigazione tra le schermate

## Struttura del progetto

```
app/src/main/java/com/example/cacciaaltesorosam/
├── data/            Entità Room (Game, Location) e modelli di supporto (PuntoTemp)
├── media/            Wrapper per registrazione (record/) e riproduzione (playback/) audio
├── ui/
│   ├── screen/
│   │   ├── master/  Flusso di preparazione caccia (nome, impostazioni, punti, riepilogo, Bluetooth)
│   │   ├── player/   Flusso di gioco lato player
│   │   └── common/   Componenti riusabili (bottoni, top bar, campo di testo, geolocalizzazione)
│   └── theme/        Palette, font e tema visivo 8-bit
└── MainActivity.kt
```

## Stato di sviluppo

- [x] Schema database (Room) e persistenza di cacce e punti
- [x] Registrazione e riproduzione audio degli indizi
- [x] Geolocalizzazione dei punti in fase di creazione
- [ ] Condivisione locale via Bluetooth (in corso)
- [ ] Flusso di gioco lato player
- [ ] Logica di prossimità durante il gioco