# Eletric Car

Projeto desenvolvido durante o curso - DIO Santander Bootcamp 2023 -  Mobile Android com Kotlin


##  Dependencias

    implementation 'com.squareup.retrofit2:retrofit:2.9.0'
    implementation 'com.squareup.retrofit2:converter-gson:2.9.0'

## Permissões
Adicionado permissão de uso internet

    <uses-permission android:name="android.permission.INTERNET" />
## Código refatorado

No desafio o aplicativo estava usando ``findViewById`` para ter acesso ao elementos do layout, alterei o código para usar ``bindid`` com isso não foi preciso usar o metodo findViewById para encontrar e interagir com os elementos do layout.

```kotlin
override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
   }
```
## Alteração código

```kotlin
    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
   }
```
## Exemplo de uso de um elemento do layout

```kotlin
    private fun setupListeners() {
        //no codigo abaixo atraves do bindig tenho acesso ao elemento do layout.
        binding.btnConfirmar.setOnClickListener {
            saveUserLocal()
        }

    }
```

## Alteração no build.gradle do Modulo: App

Adicionado o código abaixo para utilizar o binding

```kotlin
    buildFeatures{
        viewBinding = true
    }
```
