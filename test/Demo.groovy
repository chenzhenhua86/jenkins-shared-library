import com.qfcgit.devops.Deploy
import org.yaml.snakeyaml.Yaml

def readYaml(){
    def content = new File('k8s.yaml').text
    Yaml parser = new Yaml()
    def data = parser.load(content)
    def kind = data["kind"]
    def name = data["metadata"]["name"]
    println(kind)
    println(name)
}
//readYaml()




def initWorkload() {
    try {
        def content = new File('k8s.yaml').text
        Yaml parser = new Yaml()
        def data = parser.load(content)
        def kind = data["kind"]
        if (!kind) {
            throw Exception("workload file ${kind} illegal, will exit pipeline!")
        }
        workloadType = kind
        println(data)
        this.workloadNamespace = data["metadata"]["namespace"]
        if (!this.workloadNamespace){
            this.workloadNamespace = "default"
        }
        workloadName = data["metadata"]["name"]
        println(workloadName)

    } catch (Exception exc) {
        throw exc
    }
}

initWorkload()