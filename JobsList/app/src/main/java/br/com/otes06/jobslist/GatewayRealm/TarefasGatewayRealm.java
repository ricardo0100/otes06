package br.com.otes06.jobslist.GatewayRealm;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import br.com.otes06.jobslist.GatewayInterface.IListagemDeTarefasGateway;
import br.com.otes06.jobslist.GatewayRealm.Realms.TarefaRealm;
import br.com.otes06.jobslist.Structs.TarefaStruct;
import io.realm.Realm;


public class TarefasGatewayRealm implements IListagemDeTarefasGateway {

    private Context context;

    public TarefasGatewayRealm(Context context) {
        this.context = context;
    }

    @Override
    public List<TarefaStruct> buscarTodasAsTarefas() {
        Realm realm = Realm.getInstance(context);

        List<TarefaRealm> registros = realm.where(TarefaRealm.class).findAll();
        List<TarefaStruct> tarefas = new ArrayList<TarefaStruct>();

        for (TarefaRealm registro : registros) {
            TarefaStruct tarefa = new TarefaStruct();
            tarefa.setId(registro.getId());
            tarefa.setTitulo(registro.getTitulo());
            tarefa.setDescricao(registro.getDescricao());
            tarefa.setVencimento(registro.getVencimento());
            tarefas.add(tarefa);
        }

        realm.close();

        return tarefas;
    }

    @Override
    public TarefaStruct buscarPoId(int id) {
        Realm realm = Realm.getInstance(context);

        TarefaRealm registro = realm.where(TarefaRealm.class).equalTo("id", id).findFirst();
        TarefaStruct tarefa = new TarefaStruct();

        tarefa.setId(registro.getId());
        tarefa.setTitulo(registro.getTitulo());
        tarefa.setDescricao(registro.getDescricao());
        tarefa.setVencimento(registro.getVencimento());

        realm.close();
        return tarefa;
    }

    public void salvar(TarefaStruct tarefa) {
        Realm realm = Realm.getInstance(context);
        realm.beginTransaction();

        TarefaRealm registro = new TarefaRealm();
        int id = tarefa.getId();
        if (id == 0) {
            int size = ((int) realm.where(TarefaRealm.class).count());
            id = size + 10000;
        }

        registro.setId(id);
        registro.setTitulo(tarefa.getTitulo());
        registro.setDescricao(tarefa.getDescricao());
        realm.copyToRealmOrUpdate(registro);

        realm.commitTransaction();
        realm.close();
    }
}
