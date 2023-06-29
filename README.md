# Pokedex Android App

Este é um aplicativo Android que permite navegar e visualizar informações sobre diferentes Pokémons. O aplicativo utiliza a PokeAPI (https://pokeapi.co/) para buscar dados sobre Pokémon e oferece uma interface amigável para explorar seus detalhes.

## Recursos

- Visualizar uma lista de Pokémons com seus nomes e imagens (Somente pokemons da primeira geração até o número 151).
- Toque em um Pokémon para ver informações detalhadas, incluindo tipos, estatísticas e uma imagem.
- Adicione Pokémons à sua lista de favoritos.
- Armazenamento persistente dos Pokémon favoritos usando banco de dados SQLite.
- Cache dos dados de Pokémons usando SharedPreferences.

## Capturas de tela

![Captura de Tela 1](https://github.com/jinkijack/Pokedex-Android/assets/63086447/8c658610-4ea9-4e0a-9e04-74eb13296ee0)

![Captura de Tela 2](https://github.com/jinkijack/Pokedex-Android/assets/63086447/b7f614d2-7253-49f6-94b2-19ac62bf506f)
![Captura de Tela 3](https://github.com/jinkijack/Pokedex-Android/assets/63086447/76b31922-b62f-4763-a17d-adb60a02b559)
![Captura de Tela 4](https://github.com/jinkijack/Pokedex-Android/assets/63086447/9f3ce26f-3f98-48ae-900b-903e136d0fde)


## Tecnologias Utilizadas

- Android SDK
- Linguagem de programação Java
- Banco de dados SQLite para armazenamento de dados
- SharedPreferences para cache de dados
- Biblioteca Retrofit para fazer requisições à API
- Biblioteca Gson para fazer parsing de JSON
- Biblioteca Picasso para carregamento de imagens

## Primeiros Passos

Para executar o aplicativo localmente, siga estas etapas:

1. Clone este repositório: `git clone https://github.com/jinkijack/Pokedex-Android.git`
2. Abra o projeto no Android Studio.
3. Compile e execute o aplicativo em um emulador ou dispositivo físico.

## Chave de API

O aplicativo utiliza a PokeAPI pública, que não requer uma chave de API.

## Contribuições

Contribuições para este projeto são bem-vindas! Se você tiver ideias, sugestões ou relatórios de bugs, abra uma issue ou envie um pull request.
