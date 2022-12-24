package me.ericg;

public class Scoundrel {    //information holder for Blades Characters
    String name;
    String playbook;
    String alias;
    String crew;
    String heritage;
    String background;
    String vice;
    String special;
    int attune;
    int command;
    int consort;
    int finesse;
    int hunt;
    int prowl;
    int skirmish;
    int study;
    int survey;
    int sway;
    int tinker;
    int wreck;


    void setAllParameters(String newName, String newPlaybook, String newAlias, String newCrew,
                          String newHeritage, String newBackground, String newVice, String newSpecial,
                          int newAttune, int newCommand, int newConsort, int newFinesse, int newHunt,
                          int newProwl, int newSkirmish, int newStudy, int newSurvey, int newSway, int newTinker,
                          int newWreck){
            this.name = newName;
            this.playbook = newPlaybook;
            this.alias = newAlias;
            this.crew = newCrew;
            this.heritage = newHeritage;
            this.background = newBackground;
            this.vice = newVice;
            this.special = newSpecial;
            this.attune = newAttune;
            this.command = newCommand;
            this.consort = newConsort;
            this.finesse = newFinesse;
            this.hunt = newHunt;
            this.prowl = newProwl;
            this.skirmish = newSkirmish;
            this.study = newStudy;
            this.survey = newSurvey;
            this.sway = newSway;
            this.tinker = newTinker;
            this.wreck = newWreck;
    }
}
