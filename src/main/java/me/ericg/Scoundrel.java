package me.ericg;

public class Scoundrel {    //information holder for Blades Characters
    private String name;
    private String playbook;
    private String alias;
    private String crew;
    private String heritage;
    private String background;
    private String vice;
    private String special;
    private int attune;
    private int command;
    private int consort;
    private int finesse;
    private int hunt;
    private int prowl;
    private int skirmish;
    private int study;
    private int survey;
    private int sway;
    private int tinker;
    private int wreck;
    private int coin;
    private int stress;
    private String trauma = "none";
    private int stash;

    void setAllParameters(String newName, String newPlaybook, String newAlias, String newCrew,
                          String newHeritage, String newBackground, String newVice, String newSpecial,
                          int newAttune, int newCommand, int newConsort, int newFinesse, int newHunt,
                          int newProwl, int newSkirmish, int newStudy, int newSurvey, int newSway, int newTinker,
                          int newWreck, int newCoin, int newStress, String newTrauma, int newStash){
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
            this.coin = newCoin;
            this.stress = newStress;
            this.trauma = newTrauma;
            this.stash = newStash;
    }

    public int getInsight(){
        int insight = 0;

        if(hunt > 0){insight++;}
        if(study > 0){insight++;}
        if(survey > 0){insight++;}
        if(tinker > 0){insight++;}

        return insight;
    }

    public int getProwess(){
        int prowess = 0;

        if(finesse > 0){prowess++;}
        if(prowl > 0){prowess++;}
        if(skirmish > 0){prowess++;}
        if(wreck > 0){prowess++;}

        return prowess;
    }

    public int getResolve(){
        int resolve = 0;

        if(attune > 0){resolve++;}
        if(command > 0){resolve++;}
        if(consort > 0){resolve++;}
        if(sway > 0){resolve++;}

        return resolve;
    }
    public void setName(String name){this.name = name;}
    public void setPlaybook(String playbook){this.playbook = playbook;}
    public void setAlias(String alias) {this.alias = alias;}
    public void setBackground(String background) {this.background = background;}
    public void setHeritage(String heritage){this.heritage = heritage;}
    public void setCrew(String crew){this.crew = crew;}
    public void setSpecial(String special) {this.special = special;}
    public void setAttune(int attune) {this.attune = attune;}
    public void setCommand(int command) {this.command = command;}
    public void setConsort(int consort) {this.consort = consort;}
    public void setFinesse(int finesse) {this.finesse = finesse;}
    public void setHunt(int hunt) {this.hunt = hunt;}
    public void setProwl(int prowl) {this.prowl = prowl;}
    public void setSkirmish(int skirmish) {this.skirmish = skirmish;}
    public void setStudy(int study) {this.study = study;}
    public void setSurvey(int survey) {this.survey = survey;}
    public void setSway(int sway) {this.sway = sway;}
    public void setTinker(int tinker) {this.tinker = tinker;}
    public void setVice(String vice) {this.vice = vice;}
    public void setWreck(int wreck) {this.wreck = wreck;}
    public void setCoin(int coin){this.coin = coin;}
    public void setStress(int stress) {this.stress = stress;}
    public void setTrauma(String trauma) {this.trauma = trauma;}

    public String getName(){return name;}
    public String getPlaybook(){return playbook;}
    public String getAlias(){return alias;}
    public String getCrew(){return crew;}
    public String getHeritage(){return heritage;}
    public String getBackground(){return background;}
    public String getVice(){return vice;}
    public String getSpecial(){return special;}
    public int getAttune(){return attune;}
    public int getCommand(){return command;}
    public int getConsort(){return consort;}
    public int getHunt(){return hunt;}
    public int getProwl(){return prowl;}
    public int getSkirmish(){return skirmish;}
    public int getFinesse() {return finesse;}
    public int getStudy(){return study;}
    public int getSurvey(){return survey;}
    public int getSway(){return sway;}
    public int getTinker(){return tinker;}
    public int getWreck(){return wreck;}
    public int getCoin(){return coin;}
    public int getStress(){return stress;}
    public String getTrauma(){return trauma;}
    public int getStash(){return stash;}
}
