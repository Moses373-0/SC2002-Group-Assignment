# Turn-Based Combat Arena
> **SC2002 Object-Oriented Design & Programming — Group Assignment**  
> Nanyang Technological University, AY2025/26 Semester 2
A fully functional, CLI-based turn-based combat game built in Java, demonstrating Object-Oriented Design principles, SOLID compliance, and design patterns.
---
## 📋 Table of Contents
- [Overview](#overview)
- [Features](#features)
- [Architecture](#architecture)
- [Project Structure](#project-structure)
- [Getting Started](#getting-started)
- [How to Play](#how-to-play)
- [Design Principles (SOLID)](#design-principles-solid)
- [Design Patterns](#design-patterns)
- [Diagrams Index](#diagrams-index)
---
<a name="overview"></a>
## Overview
Players select a character class (Warrior or Wizard), equip items, choose a difficulty level, and engage in round-based combat against waves of enemies. The game features dynamic turn ordering, status effects, special skills with cooldowns, and backup enemy spawns.
---
<a name="features"></a>
## Features
- **2 Player Classes** — Warrior (Shield Bash + stun) and Wizard (Arcane Blast + AoE + ATK buff)
- **3 Difficulty Levels** — Easy, Medium (with backup wave), Hard (with large backup wave)
- **3 Consumable Items** — Potion (heal), Power Stone (free skill), Smoke Bomb (invulnerability)
- **Status Effects** — Stun, Defense Boost, Smoke Bomb Shield, each with duration tracking
- **Dynamic Turn Order** — Speed-based or Player-first strategies, swappable at game start
- **Backup Spawn System** — Reinforcement enemies appear mid-battle when initial wave is nearly defeated
- **Replay System** — Play again with the same or new settings after each battle
---
<a name="architecture"></a>
## Architecture
The project follows the **BCE (Boundary-Control-Entity)** architecture:
| Layer | Package | Classes |
|-------|---------|---------|
| **Boundary** | `boundary` | `GameUI` — all CLI input/output |
| **Control** | `controller` | `GameManager` — game flow, `BattleEngine` — battle logic |
| **Entity** | `entity.*` | `Combatant`, `Action`, `Item`, `StatusEffect`, `Level` hierarchies |
### UML Class Diagram
![UML Class Diagram](diagrams/SC2002_UML_Class_Diagram.png)
---
<a name="project-structure"></a>
## Project Structure
```
Assignment/
├── src/
│   ├── Main.java                              # Entry point
│   ├── boundary/
│   │   └── GameUI.java                        # All CLI I/O (Boundary)
│   ├── controller/
│   │   ├── GameManager.java                   # Game flow orchestrator (Control)
│   │   ├── BattleEngine.java                  # Battle round management (Control)
│   │   └── turnorder/
│   │       ├── TurnOrderStrategy.java         # Strategy interface
│   │       ├── SpeedBasedTurnOrder.java        # Speed-based ordering
│   │       └── PlayerFirstTurnOrder.java       # Player-first ordering
│   └── entity/
│       ├── action/
│       │   ├── Action.java                    # Action interface + TargetType enum
│       │   ├── BasicAttack.java               # Single-target attack
│       │   ├── Defend.java                    # Defense buff
│       │   ├── UseItem.java                   # Item consumption
│       │   └── SpecialSkill.java              # Class-specific skill
│       ├── combatant/
│       │   ├── Combatant.java                 # Abstract base (Entity)
│       │   ├── player/
│       │   │   ├── Player.java                # Abstract player
│       │   │   ├── Warrior.java               # Shield Bash specialist
│       │   │   └── Wizard.java                # Arcane Blast specialist
│       │   └── enemy/
│       │       ├── Enemy.java                 # Abstract enemy
│       │       ├── Goblin.java                # Fast, low HP
│       │       ├── Wolf.java                  # Balanced stats
│       │       ├── EnemyActionStrategy.java   # Strategy interface
│       │       └── BasicAttackStrategy.java   # Default AI
│       ├── effect/
│       │   ├── StatusEffect.java              # Abstract effect base
│       │   ├── StunEffect.java                # Prevents action
│       │   ├── DefendEffect.java              # +DEF bonus
│       │   └── SmokeBombEffect.java           # Negates damage
│       ├── item/
│       │   ├── Item.java                      # Item interface
│       │   ├── Potion.java                    # Heals 50 HP
│       │   ├── PowerStone.java                # Free special skill
│       │   └── SmokeBomb.java                 # 1-turn invulnerability
│       └── level/
│           ├── Level.java                     # Enemy wave configuration
│           └── Difficulty.java                # EASY/MEDIUM/HARD enum
├── diagrams/
│   ├── SC2002_UML_Class_Diagram.png           # Main class diagram (PNG)
│   ├── SC2002_UML_Class_Diagram.svg           # Main class diagram (SVG)
│   ├── UML_Class_Diagram.drawio               # Editable draw.io source
│   ├── UML_Class_Diagram.puml                 # PlantUML source
│   ├── class_diagram.png                      # Alternative diagram view
│   ├── class_diagram1.png                     # Detailed diagram view
│   └── Class Diagram1.jpg                     # Full system diagram
├── .gitignore
└── README.md
```
---
<a name="getting-started"></a>
## Getting Started
### Prerequisites
- **Java JDK 17+** installed
- Terminal / Command Prompt
### Compile
```bash
javac -d bin src/Main.java src/boundary/*.java src/controller/*.java src/controller/turnorder/*.java src/entity/action/*.java src/entity/combatant/*.java src/entity/combatant/player/*.java src/entity/combatant/enemy/*.java src/entity/effect/*.java src/entity/item/*.java src/entity/level/*.java
```
### Run
```bash
java -cp bin Main
```
---
<a name="how-to-play"></a>
## How to Play
1. **Select Character** — Choose Warrior (tanky, single-target stun) or Wizard (AoE damage + ATK buff)
2. **Pick 2 Items** — Potion, Power Stone, or Smoke Bomb (each consumable, one-time use)
3. **Choose Difficulty** — Easy (3 Goblins), Medium (1 Goblin + 1 Wolf + backup), Hard (2 Goblins + large backup)
4. **Choose Turn Order** — Speed-based (fastest goes first) or Player-first
5. **Battle!** — Each round: view status → select action → resolve all turns → repeat
6. **Win Condition** — Eliminate all enemies (including backup wave)
7. **Lose Condition** — Player HP reaches 0
### Combat Actions
| Action | Description |
|--------|-------------|
| **Basic Attack** | Deal `ATK - DEF` damage to one enemy |
| **Defend** | Gain +10 DEF until next turn |
| **Use Item** | Consume a selected item from inventory |
| **Special Skill** | Class-specific ability (3-turn cooldown) |
---
<a name="design-principles-solid"></a>
## Design Principles (SOLID)
| Principle | Where Applied |
|-----------|---------------|
| **SRP** | `Combatant` manages state only, `BattleEngine` manages flow only, `GameUI` handles I/O only |
| **OCP** | `BattleEngine` uses `List<Action>` — new actions are added by implementing `Action` interface without modifying engine code |
| **LSP** | `Warrior`/`Wizard` are fully substitutable as `Player`; `Goblin`/`Wolf` as `Enemy` |
| **ISP** | `Action`, `Item`, `TurnOrderStrategy`, `EnemyActionStrategy` — each is a focused, minimal interface |
| **DIP** | `BattleEngine` depends on `Action` (interface), `TurnOrderStrategy` (interface), `Combatant` (abstract) — never on concrete classes |
---
<a name="design-patterns"></a>
## Design Patterns
| Pattern | Implementation |
|---------|---------------|
| **Strategy** | `TurnOrderStrategy` — swap turn-ordering algorithms; `EnemyActionStrategy` — swap enemy AI |
| **Template Method** | `StatusEffect` — abstract `onApply()`/`onTurnStart()`/`onRemove()` with concrete `tick()`/`isExpired()` |
| **Factory Method** | `GameManager.createPlayer()`, `createItem()`, `createLevel()`, `createActions()` |
| **Dependency Injection** | `BattleEngine` receives `List<Action>` and `TurnOrderStrategy` via constructor |
---
<a name="diagrams-index"></a>
## Diagrams Index
All diagrams are stored in the [`diagrams/`](diagrams/) directory:
| File | Description | Format |
|------|-------------|--------|
| [`UML Class Diagram`](diagrams/UML Class Diagram.png) | **Main UML class diagram** — colour-coded BCE layers with OO annotations | PNG |
| [`scenario1-shield-bash.png`](diagrams/scenario1-shield-bash.png) | **Scenario 1:** Warrior Executes Shield Bash on Goblin | PNG |
| [`scenario2-potion.png`](diagrams/scenario2-potion.png) | **Scenario 2:** Warrior Uses Potion to Heal | PNG |
| [`scenario3-goblin.png`](diagrams/scenario3-goblin.png.png) | **Scenario 3:** Goblin Executes Basic Attack on Warrior | PNG |
---
## 📄 License
This project was developed as an academic assignment for NTU SC2002.
