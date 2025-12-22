# Tank Software Design Platformer Game

## Архитектура проекта

Проект следует принципам **Луковой архитектуры (Onion Architecture)**:

```mermaid
graph TD
    subgraph Config [Config & Entry Point]
        L[GameDesktopLauncher]
        C[GameConfig]
    end

    subgraph Infrastructure [Infrastructure Layer]
        G[Graphics (LibGDX)]
        I[Input Impl]
        AI[AI Impl]
    end

    subgraph Application [Application Layer]
        GL[GameLoop]
        LL[LogicalLevel]
        CMD[Commands]
    end

    subgraph Domain [Domain Layer]
        E[Entities]
        Interfaces[Interfaces]
    end

    %% Dependencies
    Config --> Infrastructure
    Config --> Application
    Config --> Domain

    Infrastructure --> Application
    Infrastructure --> Domain

    Application --> Domain
```

- **Domain** (`ru.mipt.bit.platformer.domain`): Центральный слой. Содержит бизнес-сущности (`Tank`, `Bullet`) и интерфейсы. Не зависит от внешних библиотек.
- **Application** (`ru.mipt.bit.platformer.application`): Слой вариантов использования (Use Cases). Управляет потоком игры (`GameLoop`), логикой уровня и командами. Зависит только от Domain.
- **Infrastructure** (`ru.mipt.bit.platformer.infrastructure`): Слой деталей реализации. Реализует интерфейсы из Domain/Application. Здесь находится код, связанный с LibGDX (графика, ввод) и конкретные реализации алгоритмов.
- **Config** (`ru.mipt.bit.platformer.config`): Слой конфигурации. Использует Spring для связывания всех компонентов воедино (Dependency Injection).

## Требования

- Java 17 или выше
- Gradle 8.5+
- macOS (поддержка других платформ через соответствующий бэкенд LibGDX)

```bash
brew install openjdk@17
java --version
```

## Сборка проекта

### Без тестов

```bash
export JAVA_HOME=/opt/homebrew/opt/openjdk@17
./gradlew clean build -x test
```

### Сборка с тестами

```bash
export JAVA_HOME=/opt/homebrew/opt/openjdk@17
./gradlew clean build
```

## Запуск игры

```bash
./gradlew run
```

Или с опцией --no-daemon:

```bash
export JAVA_HOME=/opt/homebrew/opt/openjdk@17
./gradlew run --no-daemon
```

## Тестирование

### Запуск всех тестов

```bash
export JAVA_HOME=/opt/homebrew/opt/openjdk@17
./gradlew test
```

### Просмотр отчета о тестах

```bash
open build/reports/tests/test/index.html
```

### Запуск конкретного теста

```bash
export JAVA_HOME=/opt/homebrew/opt/openjdk@17
./gradlew test --tests "ru.mipt.bit.platformer.classes.DirectionTest"
```

## Управление танком

- **W** или ↑ - движение вверх
- **S** или ↓ - движение вниз  
- **A** или ← - движение влево
- **D** или → - движение вправо
- **SPACE** - выстрел (пока не было в дз)

## Конфигурация карты

Карта настраивается в файле `src/main/resources/mapLayout.txt`:

- **X** - стартовая позиция танка
- **T** - дерево (препятствие)
- **_** - пустая клетка

## Генерация уровней

В `GameDesktopLauncher.java` доступны два режима:

### Режим 1: Случайная генерация

```java
mapLayout = new MapLayout(groundLayer);
```

### Режим 2: Загрузка из файла

```java
mapLayout = new MapLayout("mapLayout.txt");
```

## Создание дистрибутива

### JAR файл

```bash
export JAVA_HOME=/opt/homebrew/opt/openjdk@17
./gradlew jar
```

JAR файл будет находиться в: `build/libs/software-design-platformer-1.0-SNAPSHOT.jar`

### Запуск JAR

```bash
export JAVA_HOME=/opt/homebrew/opt/openjdk@17
java -jar build/libs/software-design-platformer-1.0-SNAPSHOT.jar
```

### Архивы дистрибутива

```bash
export JAVA_HOME=/opt/homebrew/opt/openjdk@17
./gradlew distZip distTar
```

Архивы будут в: `build/distributions/`

## Полезные команды

### Очистка

```bash
export JAVA_HOME=/opt/homebrew/opt/openjdk@17
./gradlew clean
```

### Остановка Gradle daemon

```bash
./gradlew --stop
```

### Запуск без daemon

```bash
export JAVA_HOME=/opt/homebrew/opt/openjdk@17
./gradlew run --no-daemon
```

### Просмотр задач

```bash
export JAVA_HOME=/opt/homebrew/opt/openjdk@17
./gradlew tasks
```
