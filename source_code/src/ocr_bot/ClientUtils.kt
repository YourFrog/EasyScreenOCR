package ocr_bot

/**
 *  Sprawdzeie czy mamy zaznaczonego jakąś istote
 */
fun Client.hasTarget(): Boolean {
    return enemies.any { it.isTarget }
}

/**
 *  Sprawdzenie czy nie mamy zaznaczonego potwora
 */
fun Client.noHasTarget(): Boolean {
    return !this.hasTarget()
}

/**
 *  Sprawdzenie czy na ekranie znajdują się przeciwnicy
 */
fun Client.hasEnemies(): Boolean {
    return this.enemies.isNotEmpty()
}

/**
 *  Zwraca zaznaczoną istote
 */
fun Client.getTargetCreature(): Creature? {
    return this.enemies.firstOrNull {
        it.isTarget
    }
}

/**
 *  Rzucenie zaklęcia przez klienta gry, zwraca informacje czy rzucenie się powiodło
 */
fun Client.castSpell(spell: ScriptInterface.Spell): Boolean {
    if( mana.current < spell.cost ) {
        return false
    }

    if( isAllowToCast(spell) ) {
        pressAndRelease(spell.key)
    } else {
        return false
    }

    return true
}

/**
 *  Rzuca pierwsze zaklęcie na które posiada mane
 */
fun Client.castFirstSpell(vararg spells: ScriptInterface.Spell): Boolean {
    val allowSpells = spells.filter {
        mana.current >= it.cost
    }.filter {
        isAllowToCast(it)
    }

    if( allowSpells.isEmpty() ) {
        return false
    }

    val result = allowSpells.firstOrNull {
        castSpell(it)
    }

    if( result == null ) {
        return false
    }

    return true
}

/**
 *  Sprawdzenie czy zaklęcie znajduje się na cd'ku
 */
fun Client.isAllowToCast(spell: ScriptInterface.Spell): Boolean {
    return spell.groups.filterNot {
        when(it) {
            ScriptInterface.Spell.Group.ATTACK -> isAttackCooldown
            ScriptInterface.Spell.Group.HEALING -> isHealingCooldown
            ScriptInterface.Spell.Group.SUPPORT -> isSupportCooldown
            else -> true
        }
    }.isNotEmpty()
}

/**
 *  Użycie przedmiotu z paska akcji
 */
fun Client.useItem(item: ScriptInterface.Item): Boolean {

    pressAndRelease(item.key)

    return true
}

/**
 *  Naciśnięcie i puszczenie przycisku
 */
fun Client.pressAndRelease(key: Int) {
    if( IS_DEBUG ) return

    keyboard.keyPress(key)
    keyboard.keyRelease(key)
}