# 🎸 Luthier Survival (place holder name) - AI Development Guide

## Project Overview

**Luthier Survival** is a top-down survival shooter with crafting and resource management. You are a luthier surviving a zombie apocalypse.

- **DAY**: Farm materials from a magic tree, craft custom guitars to fulfill customer orders, earn money
- **NIGHT**: Defend your workshop (center of screen) by manually shooting your upgradeable guitar-weapon against zombies coming from all 360°

**Tagline**: "Craft by day. Shred by night."

---

## Tech Stack

| Technology | Purpose |
|------------|---------|
| **LibGDX** | Game framework |
| **Java 21** | Language |
| **Gradle** | Build system |
| **Desktop (LWJGL3)** | Primary platform |
| **HTML (optional)** | Web export |

---

## Project Structure

```
game/
├── core/src/main/java/io/github/matheusspacifico/
│   ├── Main.java                 # Entry point, game initialization
│   ├── screens/                  # Game screens
│   │   ├── GameScreen.java       # Main gameplay
│   │   ├── MenuScreen.java       # Main menu
│   │   ├── SettingsScreen.java   # Display settings
│   │   └── ...
│   ├── entities/                 # Game entities
│   │   ├── Player.java
│   │   ├── Projectile.java
│   │   └── ...
│   ├── systems/                  # Game systems
│   │   ├── WeaponSystem.java
│   │   └── ...
│   └── utils/                    # Utilities
│       └── Constants.java
├── assets/                       # Game assets
│   ├── fonts/                    # TTF fonts (OpenSans.ttf)
│   └── ...
└── lwjgl3/                       # Desktop launcher
```

---

## Game Constants

```java
// Screen
SCREEN_WIDTH = 1920
SCREEN_HEIGHT = 1080
SPRITE_SCALE = 2.5  // Scales pixel art for higher resolution

// Display modes (configured in Lwjgl3Launcher.java and SettingsScreen.java)
// "windowed" - standard window (1280x720)
// "borderless" - borderless windowed (default, fills screen)

// Sprites (16-bit pixel art style, scaled by SPRITE_SCALE at runtime)
PLAYER_SIZE = 32x32 (rendered at 80x80)
HOUSE_SIZE = 64x64 or 96x96
ZOMBIE_SIZE = 24x24 or 32x32
TREE_SIZE = 48x48 or 64x64
PROJECTILE_SIZE = 8x8 (rendered at 20x20)

// Player (speeds scaled for larger resolution)
PLAYER_SPEED = 375 px/s (150 * SPRITE_SCALE)
PLAYER_SPRINT_SPEED = 625 px/s (250 * SPRITE_SCALE)

// Day/Night Cycle (Spring baseline)
DAY_DURATION = 12 hours (game time)
NIGHT_DURATION = 12 hours (game time)

// House
HOUSE_INITIAL_HP = 100
```

---

## Core Mechanics Summary

### Day Phase (Chill)
1. Chop magic tree (E key) → drops wood materials
2. Check mailbox for customer orders
3. Craft guitars at workbench (match order specs)
4. Deliver guitars → earn money + rare materials
5. Buy upgrades (guitar, house, defense slots)

### Night Phase (Action)
1. House is CENTER of screen (what you defend)
2. Player moves 360° around the house (WASD)
3. Aim with mouse, shoot with click
4. Zombies spawn from ALL edges, pathfind to house
5. Survive until dawn

### Guitar (Main Weapon)
- NOT swappable, only UPGRADEABLE
- Components: Body, Pickups, Strings
- Each upgrade changes stats AND visuals
- Special abilities unlock later

### Seasons (28 days each)
- 🌸 Spring: Tutorial, easy zombies, balanced day/night
- ☀️ Summer: Longer days, fire enemies
- 🍂 Autumn: Best crafting materials, bigger waves
- ❄️ Winter: Long nights (16h!), tank zombies, survival mode

---

## Code Style Guidelines

1. **Keep it simple** - This is a hobby project, readability over cleverness
2. **One class, one responsibility** - Small, focused classes
3. **Use constants** - No magic numbers, define in Constants.java
4. **Comment the "why"** - Not the "what"
5. **Game states** - Use enums for DAY/NIGHT, screen states, etc

---

## MVP Scope (v1.0)

### Must Have
- [ ] Top-down player movement (WASD)
- [ ] Mouse aim & shoot
- [ ] Day/night cycle (timer-based)
- [ ] Magic tree chopping mechanic
- [ ] 3 wood types
- [ ] Workbench crafting (3 basic guitars)
- [ ] Order system (2-3 random customers)
- [ ] Mailbox (accept/deliver orders)
- [ ] Money & basic shop (2-3 upgrades)
- [ ] Central house (HP, can be destroyed)
- [ ] 2 zombie types
- [ ] 360° zombie spawning
- [ ] 1 working season (Spring)
- [ ] 7 days to "win" MVP
- [ ] Local save/load

### Nice to Have
- [ ] Sound effects
- [ ] 1 buyable defense slot
- [ ] Boss on day 7

---

## Current Sprint Focus

> Update this section as you progress

**Completed**:
- [x] Project structure setup
- [x] Player entity with WASD movement
- [x] Basic rendering and shooting
- [x] Menu system (MenuScreen, SettingsScreen)
- [x] FreeType fonts for crisp UI text
- [x] FitViewport for resolution independence

**Current Goal**: Game World and House Entity

**Next Steps**:
1. GameWorld entity management (02_game_world.md)
2. House entity - central defense target (03_house_entity.md)
3. Day/Night cycle system (04_day_night_cycle.md)

---

## Zombie Types Reference

| Name | HP | Speed | Damage | Season |
|------|-----|-------|--------|--------|
| Garage Band Zombie | 30 | 50 | 5 | Spring |
| Groupie Zombie | 25 | 70 | 3 | Spring |
| Roadie Zombie | 80 | 40 | 10 | Summer |
| Black Metal Zombie | 150 | 60 | 20 | Winter |

---

## Guitar Components Reference

**Bodies** (affect damage):
Basswood → Maple → Mahogany → Koa

**Pickups** (affect fire rate & pattern):
Single-Coil → Humbucker → P90 → Active

**Strings** (tradeoff):
Light .009 (fast, low dmg) → Medium .010 → Heavy .012 (slow, high dmg)

---

## Art Style Notes

- 16-bit pixel art
- Colorful, not dark
- "Cozy horror" aesthetic
- Zombies are funny, not scary
- Hand-drawn sprites by the team

---

## Notes for AI Assistant

- This is a **hobby project** - keep suggestions simple and fun
- **LibGDX** is the framework - use its patterns (Screen, SpriteBatch, etc)
- Pixel art sprites will be created manually by the team
- Two developers working on this - keep code modular for easy collaboration
