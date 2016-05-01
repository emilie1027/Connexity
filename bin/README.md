# Connexity

Connexity is a Technology-driven marketing solutions company which connect retailers to the customers we care about. Our goal is to use our shopping insights to drive sales for our partners.

### Challenges
- API integration
- data utilization
- additional information collection
- flow completion

### Tech
- Java
- Spring MVC
- UI component
- Interaction with API

## Timeline & Features

**Version 1 (Mid-term Demo):** Include a search utility that ranks the products according to prices.

# Git Control Flow

We would like to keep a `dev` branch which is where all the development will be merged into. There would also be a `backend` branch and a `frontend` branch that will contain different parts of the project. The `release` branch will contain our working prototypes and will be updated with new features.

Each person will work on a different branch of their own named `<name>-<feature` (e.g. `qingwei-spring`).

When merging branches, include the `--no-ff` option to keep track of what is happening with the merging, e.g. merging `backend` into `dev` will be

```
$ git checkout dev
$ git merge --no-ff backend -m "Merge branch 'backend' into dev"
```
