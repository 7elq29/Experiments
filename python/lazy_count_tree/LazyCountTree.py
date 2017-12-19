class TreeNode:
    def __init__(self, val, count=0):
        self.count = count
        self.lazy_count = 0
        self.val = val
        self.left = None
        self.right = None


def insert(node, val):
    if node.val > val:
        if node.left is None:
            node.left = TreeNode(val, count=node.count+node.lazy_count)
        else:
            insert(node.left, val)
        node.lazy_count += 1
    elif node.val < val:
        if node.right is None:
            node.right = TreeNode(val, count=node.count+node.lazy_count+1)
        else:
            node.right.lazy_count=node.lazy_count
            insert(node.right, val)
        node.count += node.lazy_count
        node.lazy_count = 0


def count(node, val):
    if node == None:
        return -1
    if node.val == val:
        return node.count+node.lazy_count
    elif node.val > val:
        return count(node.left, val)
    else:
        cnt = count(node.right, val)
        node.count += node.lazy_count
        node.lazy_count = 0
        return cnt


root = TreeNode(6)
l = [4, 1, 5, 8, 3, 7]
print("insert {}, count:{}".format(6, 0))
for num in l:
    insert(root, num)
    print("insert {}, count:{}".format(num, count(root, num)))