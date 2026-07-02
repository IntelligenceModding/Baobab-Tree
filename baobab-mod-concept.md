# Baobab: Giants of the Drylands

> **A NeoForge 1.21.1 world-expansion mod built around colossal baobab trees, ancient savanna landmarks, harvestable fruit, living root systems, and rare hollow giants that turn the landscape into a place worth exploring.**

---

## 1. High Concept

**Baobab: Giants of the Drylands** is a standalone Minecraft NeoForge 1.21.1 mod that makes baobab trees a defining feature of dry, open landscapes.

This is not a small “new wood type” mod. The baobab is meant to feel like a natural monument: visible from far away, broad enough to shape the terrain around it, useful enough to reward exploration, and visually distinct enough that players remember where they found one.

The mod’s identity is based on three ideas:

1. **Landmarks over filler vegetation** — baobabs are uncommon and meaningful, never spammed across the landscape.
2. **Environmental storytelling** — roots, scars, fruit, hanging pods, hollows, fallen leaves, and surrounding dry vegetation make every tree feel old and lived-in.
3. **Build value without busywork** — the wood set, roots, leaves, fruit, and hollow trees give builders useful materials and spaces without turning the mod into a machine or progression mod.

The intended feeling is:

> *You see a giant silhouette on the horizon, cross the savanna to reach it, and discover that the tree itself is a place.*

---

## 2. Mod Identity

### Primary fantasy

Baobabs are ancient “world trees” of drylands: swollen trunks, thick roots, wide crowns, hanging fruit, and centuries of weathering.

### Tone

- Monumental
- Warm and dry
- Slightly mystical without becoming magical
- Rugged rather than cute
- Rich in environmental detail
- Designed for screenshots, exploration, and landmark building

### What makes it different from ordinary tree mods

- Trees use **bespoke large-scale geometry**, not standard oak-style trunk-and-leaf blobs.
- Their roots and trunk bases shape the ground around them.
- Rare trees can contain **organic hollow interiors** rather than simple decorative cavities.
- Fruit is visibly integrated into the canopy through hanging pods.
- The tree comes with a whole dryland atmosphere: leaf litter, root fragments, cracked ground accents, and old bark textures.

---

## 3. Core Gameplay Loop

1. A player explores a savanna or another compatible dryland biome.
2. They spot a baobab in the distance and use it as a landmark.
3. They harvest wood, leaves, fruit, saplings, and decorative ground cover.
4. They may discover a rare ancient baobab with a naturally formed hollow interior.
5. They use baobab materials for warm-toned builds, desert settlements, treehouses, outposts, ruins, market spaces, and custom landmarks.
6. They cultivate young baobabs near their own base, while worldgen remains the source of truly monumental trees.

The mod should reward discovery and building, not farming loops or combat grinding.

---

## 4. World Generation

## 4.1 Biome philosophy

Baobabs should primarily appear in landscapes where their silhouette can dominate the horizon.

### Default compatible biomes

- Savanna
- Savanna Plateau
- Windswept Savanna, at a lower rate and only where terrain clearance allows

### Optional modpack compatibility

The mod should expose a biome tag so modpacks can add baobabs to custom drylands, scrublands, mesas, tropical grasslands, or fantasy savannas.

Suggested tag:

```text
baobab:has_baobab_generation
```

The default tag contains only the intended vanilla biomes. Modpacks can extend it freely.

## 4.2 Generation density

Baobabs should be memorable. A player should not see one every few seconds while moving through a savanna.

Recommended default density:

| Tree type | Approximate rarity | Purpose |
|---|---:|---|
| Young Baobab | Commonest | Natural variation and player-scale trees |
| Mature Baobab | Uncommon | Main landmark tree |
| Ancient Baobab | Rare | High-impact exploration reward |
| Sacred Baobab | Extremely rare | Once-in-a-long-journey sight, optional flagship landmark |

The exact rate should be configurable, but the default should favour rarity over abundance.

## 4.3 Placement rules

A baobab should only generate when it has enough room to look intentional.

Generation should fail when:

- The ground is too steep.
- The root footprint would enter water.
- The canopy would be buried inside terrain.
- The trunk would overlap a structure or an existing large tree.
- The root system would clip into a ravine, cave opening, village, or structure.
- The full tree cannot fit inside a safe bounding area.

This is important: it is better to generate fewer trees than generate broken ones.

## 4.4 Surrounding terrain treatment

Large baobabs should subtly alter the immediate ground around them.

Possible features:

- Small patches of coarse dirt beneath the canopy.
- Sparse dry grass and occasional tall grass around the roots.
- Fallen leaf litter clusters.
- Exposed root blocks emerging through the surface.
- A rare ring of weathered stones or cracked dirt around ancient specimens.
- Occasional dead branches or root fragments near old trees.

The terrain treatment should stay restrained. It should make the tree feel embedded in the environment, not create a prefab circle around every trunk.

---

## 5. Tree Variants

## 5.1 Young Baobab

The young baobab is the version players can reliably grow from a sapling.

| Feature | Design |
|---|---|
| Height | 8–12 blocks |
| Trunk | Broad lower trunk, usually 2×2 at the base |
| Crown | Compact, irregular, slightly flattened |
| Roots | Small exposed roots extending 1–3 blocks |
| Fruit | A few hanging pods on mature branches |
| Role | Player cultivation and low-impact world variation |

Young trees should still feel distinct from standard Minecraft trees. The trunk needs visible swelling and roots even at this size.

## 5.2 Mature Baobab

This is the mod’s main landmark tree.

| Feature | Design |
|---|---|
| Height | 14–19 blocks |
| Trunk base | Uneven 3×3 to 5×5 swollen base |
| Branching | 4–7 thick branch arms spreading outward |
| Crown width | 18–28 blocks |
| Roots | Wide buttress roots wrapping across the surface |
| Fruit | Numerous pods, usually concentrated near branch undersides |
| Role | Exploration landmark and main natural source of materials |

A mature baobab should be visible over normal savanna vegetation and look recognisable from a distance.

## 5.3 Ancient Baobab

Ancient baobabs are rare, dramatic, and partially hollow.

| Feature | Design |
|---|---|
| Height | 20–27 blocks |
| Trunk base | Large 5×5 to 7×7 root mass |
| Crown width | 28–36 blocks |
| Hollow interior | Organic small chamber, not a perfect room |
| Exterior | Scarred bark, root arches, irregular old branches |
| Fruit | Heavy pod clusters, some with overripe appearance |
| Role | Major landmark, natural shelter, custom build site |

The hollow should feel formed by age, rot, and weather. It may have small cracks of sunlight, uneven walls, root protrusions, and one or more natural entrances.

## 5.4 Sacred Baobab — optional flagship feature

The Sacred Baobab is optional content for the first major update or a rare launch feature. It should be extremely rare and never common enough to become routine.

Characteristics:

- Much larger than ancient variants.
- A broad root system that can form arches and passageways.
- A huge canopy that creates a recognizable “island of shade.”
- Multiple interior cavities or a large natural chamber.
- Unique visual details such as pale bark scars, hanging vines, glowing fireflies at night, or clustered blossoms.

It should not contain mandatory loot, bosses, or forced progression. Its value comes from discovery and atmosphere.

---

## 6. Block and Item Content

## 6.1 Wood family

The mod includes a complete baobab wood family.

### Core wood blocks

```text
Baobab Log
Baobab Wood
Stripped Baobab Log
Stripped Baobab Wood
Baobab Planks
Baobab Stairs
Baobab Slab
Baobab Fence
Baobab Fence Gate
Baobab Door
Baobab Trapdoor
Baobab Button
Baobab Pressure Plate
Baobab Sign
Baobab Hanging Sign
Baobab Boat
Baobab Chest Boat
```

### Wood identity

- **Outer bark:** dusty gray-brown with olive-beige weathering, thick vertical folds, scars, and dry cracks.
- **Inner wood:** warm red-brown to muted copper-brown.
- **Planks:** rich earthy brown with a sun-faded, weathered feeling.
- **Doors and trapdoors:** heavy, rustic, suitable for desert forts, savanna settlements, market stalls, temples, treehouses, and ruins.

The wood should not look like recoloured acacia. Its muted, mature, weathered look is central to the set’s identity.

## 6.2 Natural foliage blocks

```text
Baobab Leaves
Baobab Sapling
Potted Baobab Sapling
Baobab Leaf Litter
Baobab Root
Baobab Root Arch
Baobab Root Fragment
Baobab Fruit Pod
```

### Baobab Leaves

- Dense, rounded leaf clusters.
- Olive-green and slightly dusty rather than tropical bright green.
- Can use biome tinting, but should retain a recognisable dryland colour profile.
- Rarely drop saplings, sticks, and fruit pods.

### Baobab Leaf Litter

A ground-cover block inspired by leaf litter mechanics but with its own dry, curled leaf texture.

Suggested behaviour:

- Thin carpet-like decorative layer.
- Stackable to several layers for a richer forest-floor effect.
- Generated beneath mature and ancient trees.
- Crafted from leaves or obtained naturally.
- Compostable.

### Baobab Roots

Roots are not merely part of the generation shape; they are a buildable decorative resource.

Suggested forms:

- **Baobab Root:** low horizontal root block.
- **Baobab Root Arch:** decorative root that creates a walkable arch shape.
- **Baobab Root Fragment:** small ground decoration, like driftwood or a dry branch.

These can be harvested with an axe and used by builders to make organic terrain, ruins, treehouses, forests, or dryland settlements.

## 6.3 Fruit and pods

```text
Baobab Fruit Pod
Baobab Fruit
Dried Baobab Pulp
Baobab Seeds
```

### Baobab Fruit Pod

Fruit pods visibly hang from leaves and suitable branches.

Growth stages:

| Stage | Appearance |
|---|---|
| 0 | Small pale bud or flower remnant |
| 1 | Green hanging pod |
| 2 | Enlarged yellow-green pod |
| 3 | Mature tan-brown pod |
| 4 | Overripe dry pod, slightly darker and more weathered |

At maturity, pods can be harvested for fruit and a small chance of seeds.

### Baobab Fruit

Base food profile:

| Property | Suggested value |
|---|---|
| Hunger restored | 3–4 points |
| Saturation | Moderate |
| Stack size | 64 |
| Composter chance | High |
| Role | Convenient exploration food |

### Dried Baobab Pulp

A simple food or ingredient created by smelting, smoking, or crafting Baobab Fruit.

Potential role:

- More portable than raw fruit.
- Slightly higher saturation.
- Can be used in a small number of aesthetic recipes such as trail rations or dryland stew.

Keep it grounded and optional. The mod should not become a food overhaul.

### Baobab Seeds

Seeds are a more intentional method of growing baobabs than random sapling drops.

Suggested logic:

- Fruit pods provide seeds.
- Seeds can be planted or crafted into saplings.
- This gives fruit harvesting a direct connection to cultivation.

---

## 7. Optional Decorative Additions

These are recommended for strong visual identity but should remain compact in scope.

## 7.1 Baobab blossoms

A small pale flower block or hanging blossom stage that appears on some trees before fruit develops.

Purpose:

- Adds seasonal-looking life to the tree.
- Makes young and flowering trees feel distinct.
- Provides extra colour contrast against dry foliage.

## 7.2 Dry vines

Short, sparse hanging vines that can appear under old branches.

Visual direction:

- Thin, brown-green, dry, and uneven.
- Less lush than jungle vines.
- Used sparingly, especially on ancient trees.

## 7.3 Weathered bark plates

Decorative bark slabs or wall-mounted bark fragments.

Use cases:

- Ruins.
- Desert market stalls.
- Dryland huts.
- Tree-based architecture.
- Organic custom trees.

## 7.4 Termite mounds — optional

Small mud-and-earth structures that can occasionally appear near baobabs.

They should be decorative terrain features only unless a later update introduces an intentionally small interaction loop.

Do not add termites as mobs unless the mod expands substantially and the mob has a real reason to exist.

---

## 8. Player-Grown Baobabs

Player-grown trees should feel rewarding but must not trivialize finding naturally generated giant trees.

### Sapling growth behaviour

- Baobab saplings grow into **young baobabs** by default.
- They require a wider clearance area than normal trees.
- Bone meal can accelerate growth.
- A sapling should fail safely if the area is blocked.
- Natural giant variants should not be easily farmable by normal bone meal alone.

### Optional growth catalyst

A simple item such as **Ancient Soil** or **Sun-Baked Compost** can be used to give players a low chance of growing a mature baobab.

This should be optional and rare enough that naturally generated mature and ancient trees remain valuable.

Possible recipe ingredients:

- Coarse dirt
- Bone meal
- Baobab leaf litter
- Baobab fruit or dried pulp

No magic particle effects or technical machinery are required.

---

## 9. Building and Decoration Value

The mod should give builders more than just planks.

### Main building uses

- Desert villages and oasis settlements
- Savanna forts
- Treehouse bases
- Market stalls
- Warm rustic interiors
- Ancient ruins
- Safari camps
- Tribal-inspired fantasy settlements
- Dryland temples
- Organic terrain builds
- Custom giant trees

### Material roles

| Material | Builder use |
|---|---|
| Baobab Planks | Warm structural wood |
| Bark Logs | Thick rustic exterior walls |
| Stripped Logs | Interior beams and trim |
| Roots | Organic terrain and treehouse supports |
| Root Arches | Natural tunnels and gateway shapes |
| Leaf Litter | Ground depth and path decoration |
| Fruit Pods | Hanging harvest details and market decoration |
| Blossoms | Soft colour accents |
| Bark Plates | Weathered walls, ruins, and trim |

---

## 10. Progression and Balance

The mod should remain useful from early game to late game without adding mandatory progression.

### Early game

- Find a baobab landmark.
- Harvest wood and fruit.
- Use it as shade, a temporary shelter, or a navigation point.
- Obtain seeds/saplings for future cultivation.

### Mid game

- Grow baobabs near a settlement or base.
- Use roots, leaf litter, wood, and fruit pods for building.
- Search for ancient specimens as build locations.

### Late game

- Use rare sacred trees as large creative landmarks.
- Incorporate the material palette into megabases, custom biomes, and organic terrain projects.

### What the mod deliberately avoids

- No mandatory boss.
- No progression gate for other Minecraft systems.
- No new combat tier.
- No machine chain.
- No overpowered food effects.
- No rare-resource exploitation loop.
- No reason to destroy every baobab you find.

The tree should feel valuable alive, not just as a pile of logs.

---

## 11. Advancements

## “A Giant on the Horizon”
**Trigger:** Discover or obtain a Baobab Log.  
**Description:** Find one of the drylands’ oldest landmarks.

## “Under Ancient Branches”
**Trigger:** Enter the canopy or root area of a mature baobab.  
**Description:** Stand beneath a giant of the savanna.

## “Fruit of the Drylands”
**Trigger:** Harvest Baobab Fruit.  
**Description:** Gather food from an ancient tree.

## “Roots Run Deep”
**Trigger:** Obtain a Baobab Root or Root Arch.  
**Description:** Take a piece of the landscape with you.

## “Home Inside a Giant”
**Trigger:** Enter an ancient baobab hollow.  
**Description:** Some trees have lived long enough to become shelters.

## “Plant a Future Giant”
**Trigger:** Grow a Baobab Sapling.  
**Description:** Even the largest landmark begins as a seed.

## “The Tree Remembers”
**Trigger:** Discover a Sacred Baobab, if included.  
**Description:** A living monument older than the path that led you here.

---

## 12. Creative Tab Presentation

The creative-tab order should feel intentional:

1. Baobab Log
2. Baobab Wood
3. Stripped Baobab Log
4. Stripped Baobab Wood
5. Baobab Planks
6. Stairs / slab / fence / fence gate
7. Door / trapdoor / button / pressure plate
8. Sign / hanging sign
9. Leaves
10. Sapling
11. Leaf Litter
12. Root
13. Root Arch
14. Root Fragment
15. Fruit Pod
16. Baobab Fruit
17. Dried Baobab Pulp
18. Seeds
19. Boat
20. Chest Boat

The icon should ideally be a **Baobab Fruit Pod** or a **small Baobab Root block**, not a generic plank.

---

## 13. Visual Direction

## 13.1 Palette

### Bark

- Dusty gray-brown
- Dry tan highlights
- Olive-beige weathering
- Deep reddish-brown cracks

### Inner wood and planks

- Burnt sienna
- Weathered copper-brown
- Faded terracotta brown
- Dark warm grain lines

### Leaves

- Olive green
- Desaturated yellow-green
- Slight dry brown edging in some detail pixels

### Fruit pods

- Pale green when young
- Yellow-green when developing
- Tan, dusty gold, and brown when mature

### Ground cover

- Dry ochre
- Faded brown
- Olive-tan

## 13.2 Texture rules

- Avoid overly smooth bark: baobab bark should have thick vertical folds and weathered scars.
- Avoid highly saturated acacia-orange wood.
- Avoid dense jungle-style leaf textures.
- Roots should look heavy, old, and integrated with bark—not like random logs placed sideways.
- Fruit pods should read as large hanging objects from a distance, not tiny berries.

## 13.3 Shape language

- Swollen trunk bases
- Wide root buttresses
- Heavy horizontal branch arms
- Broad, irregular crowns
- A few empty gaps in the canopy to let light through
- Imperfect asymmetry

The trees should feel built by age and weather, not generated from a clean geometric blueprint.

---

## 14. Sounds and Atmosphere

The mod does not need a huge sound system, but a small sound layer would greatly improve immersion.

### Suggested sounds

- Dry leaf rustle for Baobab Leaves.
- Slightly heavier wooden break sound for roots and bark.
- Soft fibrous crunch when harvesting fruit pods.
- Distinct low woody thud for large trunk blocks.

### Optional ambience around ancient trees

Very subtle and rare:

- Wind through broad branches.
- Dry leaves shifting.
- Occasional distant bird-like ambience without adding a new mob.

These should be restrained and non-intrusive.

---

## 15. Configuration

A good standalone mod should work for casual players and modpack authors without requiring edits to code or data files.

## 15.1 Common configuration options

| Setting | Default intention |
|---|---|
| Baobab worldgen enabled | Enabled |
| Young baobab density | Moderate |
| Mature baobab density | Low |
| Ancient baobab density | Very low |
| Sacred baobab density | Extremely low / optional |
| Baobabs in Windswept Savanna | Enabled at reduced rate |
| Fruit pod generation | Enabled |
| Root terrain decoration | Enabled |
| Player-grown mature baobabs | Disabled or very rare |
| Ancient hollow generation | Enabled |
| Allow biome-tag expansion | Enabled |

## 15.2 Server-side balance options

- Fruit nutrition values.
- Sapling drop chance.
- Seed drop chance.
- Bone meal growth chance.
- Whether roots are harvestable.
- Whether sacred baobabs generate.
- Maximum size of rare trees.

## 15.3 Modpack integration

The mod should support data-driven changes for:

- Biome inclusion/exclusion.
- Feature frequency.
- Structure spacing or exclusion zones.
- Loot-table additions.
- Tag-based recipe compatibility.

The goal is that a modpack can make baobabs fit a custom world without modifying source code.

---

## 16. Compatibility Expectations

The content should participate in normal Minecraft and NeoForge conventions.

### Expected compatibility

- Standard wood recipes and tags.
- Axe stripping behaviour.
- Fire and flammability behaviour appropriate for wood/leaves.
- Composting for leaves, leaf litter, fruit, and seeds.
- Boat and chest boat support.
- Sign and hanging-sign support.
- Common storage and recipe mods via standard plank/log/leaf/food tags.
- Custom-biome integration through a biome tag.

### Worldgen safety

The mod should prioritize avoiding conflicts with:

- Villages
- Outposts
- Temples
- Modded structures
- Large cave entrances
- Rivers and lakes
- Existing giant trees

A tree failing to place is always preferable to ruining another feature.

---

## 17. Release Scope

## Version 1.0 — Required content

- Young, mature, and ancient baobab variants.
- Full baobab wood set.
- Baobab leaves and saplings.
- Baobab root blocks, root arches, and root fragments.
- Fruit pods with growth stages.
- Baobab Fruit, seeds, and dried pulp.
- Baobab leaf litter.
- Savanna-focused world generation.
- Rare hollow ancient baobabs.
- Advancements.
- Configuration for worldgen and balance.
- Standard integration tags and recipes.

## Version 1.1 — Strong expansion candidates

- Blossoming tree phase.
- Dry vines.
- Weathered bark plates.
- Sacred Baobab landmark.
- More terrain dressing around old trees.
- Improved root arch variants.

## Version 1.2+ — Only if the mod needs more depth

- Termite mounds.
- Rare dryland ruins under ancient roots.
- Optional small ambient creatures.
- New savanna-specific flora designed around baobab groves.

These should only be added if they enhance the tree’s world and do not turn the project into a generic biome overhaul.

---

## 18. Explicit Non-Goals

To keep the mod focused, it should not include the following at launch:

- A new dimension.
- A full desert biome overhaul.
- A complex survival system such as thirst or temperature.
- A boss or combat progression requirement.
- A large food overhaul.
- Villagers with custom professions.
- A forced lore questline.
- Massive amounts of unrelated dryland blocks.
- Hundreds of decorative items that dilute the baobab theme.

The baobab is the main character of the mod. Everything else exists to make that tree feel more alive, more useful, and more unforgettable.

---

## 19. One-Sentence Storefront Description

**Baobab: Giants of the Drylands adds colossal baobab trees to NeoForge 1.21.1, with sprawling roots, rare hollow ancients, hanging fruit pods, dryland foliage, and a complete weathered wood set for landmark-scale exploration and building.**

---

## 20. Short Feature List for Mod Pages

- Monumental baobab trees built for savanna exploration.
- Young, mature, ancient, and optional sacred tree variants.
- Sprawling roots, root arches, and natural hollow interiors.
- Hanging baobab fruit pods with growth stages.
- A complete weathered baobab wood set.
- Leaf litter, dryland ground detail, and buildable organic roots.
- Rare landmarks that feel worth finding instead of ordinary worldgen filler.
- Configurable, biome-tag-driven world generation for modpack compatibility.

