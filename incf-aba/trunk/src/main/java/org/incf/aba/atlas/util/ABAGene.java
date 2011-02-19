package org.incf.aba.atlas.util;


public class ABAGene {
    
    // furnished values from gene
    private String genename;
    private String genesymbol;
    private String mgimarkeraccessionid;
    private String organism;
    
    // furnished values from ABA's Gene Finder
    private String energy;
    
    // derived from mgimarkeraccessionid
    private String mgiPrefix;
    private String mgiSeparator;
    private String mgiIdentifier;
    
    public void setMgimarkeraccessionid(String mgimarkeraccessionid) {
        this.mgimarkeraccessionid = mgimarkeraccessionid;
        mgiSeparator = ":";
        String[] mgiSegments = mgimarkeraccessionid.split(mgiSeparator);
        mgiPrefix = mgiSegments[0];
        mgiIdentifier = mgiSegments[1];
    }
    
    public String getMgiPrefix() {
        return mgiPrefix;
    }
    
    public String getMgiSeparator() {
        return mgiSeparator;
    }
    
    public String getMgiIdentifier() {
        return mgiIdentifier;
    }

    public String getGenename() {
        return genename;
    }
    
    public void setGenename(String genename) {
        this.genename = genename;
    }
    
    public String getGenesymbol() {
        return genesymbol;
    }
    
    public void setGenesymbol(String genesymbol) {
        this.genesymbol = genesymbol;
    }
    
    public String getMgimarkeraccessionid() {
        return mgimarkeraccessionid;
    }
    
    public String getOrganism() {
        return organism;
    }
    
    public void setOrganism(String organism) {
        this.organism = organism;
    }
    
    public String getEnergy() {
        return energy;
    }
    
    public void setEnergy(String energy) {
        this.energy = energy;
    }

}

