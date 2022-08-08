package demo

def init(String content) {
    this.content = content
    return this
}

def sayHi() {
    println("hi, " + this.content)
    return this
}

def sayBye() {
    println("bye " + this.content)
}