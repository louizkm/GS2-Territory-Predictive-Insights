package br.edu.fiap.territorio.dominio;

public abstract class ComponenteTerritorial {
    private final String id;
    private final String nome;
    private final String regiao;
    private StatusComponente status;

    protected ComponenteTerritorial(String id, String nome, String regiao) {
        this.id = id;
        this.nome = nome;
        this.regiao = regiao;
        this.status = StatusComponente.OPERACIONAL;
    }

    public abstract StatusComponente verificarStatus();

    public String getId() { return id; }
    public String getNome() { return nome; }
    public String getRegiao() { return regiao; }
    public StatusComponente getStatus() { return status; }
    protected void setStatus(StatusComponente status) { this.status = status; }
}
