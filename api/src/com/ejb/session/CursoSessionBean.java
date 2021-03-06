package com.ejb.session;

import java.util.List;

import javax.ejb.Stateless;
import javax.jws.WebService;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.jboss.ws.api.annotation.WebContext;

import com.ejb.intf.CursosSession;
import com.model.ejb.entity.Curso;
import com.model.ejb.entity.Disciplina;

@Stateless
@WebService(name="cursos")
@WebContext(contextRoot="universidade")
public class CursoSessionBean implements CursosSession {

	@PersistenceContext(unitName="Universidade")
	private EntityManager em;
	
	@Override
	public void remove(Curso p) {
		Curso c = em.find(Curso.class, p.getId());
		if(c != null)
			em.remove(c);
	}

	@Override
	public Curso insere(Curso c) {
		em.persist(c);
		return c;
	}

	@Override
	public Curso altera(Curso c) {
		em.merge(c);
		return c;
	}

	@Override
	public Curso buscaPorId(Long id) {
		Curso c = em.find(Curso.class, id);
		return c;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Curso> buscaTodos() {
		Query q = em.createNamedQuery("busca.todos.cursos");
		return q.getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Disciplina> buscaDisciplinas(Curso c) {
		Query q = em.createNamedQuery("busca.disciplinas.curso").setParameter("curso", c);
		return q.getResultList();
	}

}
