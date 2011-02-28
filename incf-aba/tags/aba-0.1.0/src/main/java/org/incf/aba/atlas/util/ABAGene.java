package org.incf.aba.atlas.util;


public class ABAGene {
    
    // furnished values from ABA's Gene Finder
	// e.g. http://mouse.brain-map.org/agea/GeneFinder.xml?seedPoint=6600,4000,5600
    private String energy;
    private String genesymbol;
    
    // furnished values from gene
    // e.g. http://www.brain-map.org/aba/api/gene/Plxnb1.xml
    private String genename;
    private String mgimarkeraccessionid;
    private String organism;
    
    // derived from mgimarkeraccessionid
    private String mgiPrefix;
    private String mgiSeparator;
    private String mgiIdentifier;
    
    // identifier assigned by application
    private String id;
    
    public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setMgimarkeraccessionid(String mgimarkeraccessionid) {
        this.mgimarkeraccessionid = mgimarkeraccessionid;
        mgiSeparator = ":";				// hard code!
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
    
    public String toString() {
    	return String.format("ABAGene id: %s, genesymbol: %s, genename: %s, "
    			+ "energy: %s, organism: %s, mgimarkeraccessionid: %s, "
    			+ "mgiPrefix: %s, mgiSeparator: %s, mgiIdentifier: %s", 
    			id, genesymbol, genename, energy, organism, 
    			mgimarkeraccessionid, mgiPrefix, mgiSeparator, mgiIdentifier);
    }

}

