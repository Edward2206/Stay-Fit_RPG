package com.edward_costache.stay_fitrpg;

public class User {
    private String username;
    private double level;
    private int strength;
    private int agility;
    private int stamina;
    private int health;

    public User()
    {

    }

    public User(String username) {
        setUsername(username);
        setLevel(1);
        setStrength(1);
        setAgility(1);
        setStamina(1);
        setHealth(50);
    }

    public User(String username, double level, int strength, int agility, int stamina, int health)
    {
        setUsername(username);
        setLevel(level);
        setStrength(strength);
        setAgility(agility);
        setStamina(stamina);
        setHealth(health);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public double getLevel() {
        return level;
    }

    public void setLevel(double level) {
        this.level = level;
    }

    public int getStrength() {
        return strength;
    }

    public void setStrength(int strength) {
        this.strength = strength;
    }

    public int getAgility() {
        return agility;
    }

    public void setAgility(int agility) {
        this.agility = agility;
    }

    public int getStamina() {
        return stamina;
    }

    public void setStamina(int stamina) {
        this.stamina = stamina;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }
}
